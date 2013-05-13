
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "bean")
@SessionScoped
public class BackingBean implements Serializable {

    private TreeNode root;
    public TreeNode selectedNode;
    public String removeString;
    //
    //Question attributes must also be added to the Question class
    private String questionText;
    public boolean isRequired = false;
    public String errorMessage = "This question is required.";
    public String toolTip;
    private String type = "none";
    public int minLength;//textbox
    public int maxLength;//textbox
    public String validCharacters;//textbox
    public boolean validateFormat;//textbox
    public boolean validateContents;//textbox   
    private List<Page> pages;

    private class MyTreeNode extends DefaultTreeNode {

        int index;

        private MyTreeNode(String text, TreeNode parent, int index) {
            super(text, parent);
            this.index = index;
        }
    }

    public BackingBean() {
        pages = new ArrayList<Page>();
        pages.add(new Page("Page 1", 0));

        loadPages();
    }

    private void loadPages() {
        root = new MyTreeNode("Root", null, -1);
        TreeNode previouslySelected = selectedNode;
        MyTreeNode node;
        int pageIndex = 0;
        for (Page p : pages) {
            node = new MyTreeNode(p.name, root, pageIndex++);
            node.setExpanded(true);
            int questionIndex = 0;
            System.out.println(p.name);
            for (Question q : p.questions) {
                new MyTreeNode(q.name, node, questionIndex++);
                System.out.println(q.name);
            }
        }
        System.out.println();
        selectedNode = previouslySelected;
        if (selectedNode == null) {
            selectedNode = root.getChildren().get(0).getChildren().get(0);
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        System.out.println("current: " + selectedNode.getData().toString());
        System.out.println("switching to: " + event.getTreeNode().getData().toString());
        System.out.flush();
        if (selectedNode == root) {
            selectedNode = root.getChildren().get(0).getChildren().get(0);
        }
        if (!selectedNode.equals(event.getTreeNode())) {
            RequestContext context = RequestContext.getCurrentInstance();
            boolean isPageNode = selectedNode.getParent().equals(root);
            int pageIndex;
            int questionIndex;
            if (isPageNode) {
                System.out.println("current node is a page node - nothing to save");
                //String sPageIndex = selectedNode.getData().toString().substring(5);
                //TODO this will show how the page will look
            } else {
                System.out.println("current node is a question node - saving state");
                pageIndex = Integer.parseInt(selectedNode.getParent().getData().toString().substring(5)) - 1;
                questionIndex = Integer.parseInt(selectedNode.getData().toString().substring(9)) - 1;
                System.out.println("the current question's text is: " + questionText);
                //setQuestion(pageIndex, questionIndex);
                //clearQuestion();
            }
            selectedNode = event.getTreeNode();
            removeString = determineRemoveString();
            isPageNode = selectedNode.getParent().equals(root);
            if (isPageNode) {
                context.execute("showPage();");
            } else {
//                pageIndex = Integer.parseInt(selectedNode.getParent().getData().toString().substring(5)) - 1;
//                questionIndex = Integer.parseInt(selectedNode.getData().toString().substring(9)) - 1;
//                getQuestion(pageIndex, questionIndex);
//                System.out.println("the new question's type is: " + type);
                context.execute("showCorrectQuestionSettings();");
            }
            context.execute("test();");
            //context.update("form:mainSection");//do it with javascript instead? pass key-value pairs for the ids and their values? maybe even their types?
            //context.update("form:messages");
        } else {
            System.out.println("node is already selected");
        }
    }

    private String determineRemoveString() {
        if (selectedNode == null) {
            return "You must first select a page or question to remove.";
        }
        boolean isPageNode = selectedNode.getParent().equals(root);
        boolean onlyOne = selectedNode.getParent().getChildCount() == 1;
        if (isPageNode) {
            if (onlyOne) {
                return "This is the only page so it cannot be removed.";
            } else {
                return "Are you sure you want to remove this page? Doing so will also remove all questions on this page.";
            }
        } else {
            if (onlyOne) {
                return "This is the only question on this page so it cannot be removed unless you remove the entire page.";
            } else {
                return "Are you sure you want to remove this question?";
            }
        }
    }

    public void addQuestion() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (selectedNode != null && selectedNode.getData().toString().startsWith("Page ")) {
            int questionIndex = selectedNode.getChildCount();
            String sPageIndex = selectedNode.getData().toString().substring(5);
            int pageIndex = Integer.parseInt(sPageIndex);
            pages.get(pageIndex - 1).addQuestion("Question " + (questionIndex + 1), questionIndex);
            loadPages();
        } else {
            facesContext.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must first select a page.", ""));
        }
        context.update("form:messages");
    }

    public void addPage() {
        RequestContext context = RequestContext.getCurrentInstance();
        pages.add(new Page("Page " + (pages.size() + 1), pages.size()));
        loadPages();
        context.update("form:messages");
    }

    public void checkNode(String operation) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (selectedNode == null) {
            facesContext.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must first select an item to remove.", ""));
            context.update("form:messages");
            return;
        }
        boolean isPageNode = selectedNode.getParent().equals(root);
        boolean onlyOne = selectedNode.getParent().getChildCount() == 1;
        if (isPageNode) {
            if (!operation.equals("remove page")) {
                facesContext.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must select a page (not a question) to remove a page.", ""));
            } else if (onlyOne) {
                facesContext.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, removeString, ""));
            } else {
                context.execute("confirmationDialog.show()");
            }
        } else {
            if (!operation.equals("remove question")) {
                facesContext.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must select a question (not a page) to remove a question.", ""));
            } else if (onlyOne) {
                facesContext.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, removeString, ""));
            } else {
                context.execute("confirmationDialog.show()");
            }
        }
        context.update("form:messages");
    }

    public void removeNode() {
        TreeNode parent = selectedNode.getParent();
        boolean isPageNode = selectedNode.getParent().equals(root);
        String sIndex;
        if (isPageNode) {
            sIndex = selectedNode.getData().toString().substring(5);
            pages.remove(getPage("Page " + sIndex));
        } else {
            sIndex = selectedNode.getData().toString().substring(9);
            int pageIndex = Integer.parseInt(parent.getData().toString().substring(5));
            pages.get(pageIndex).questions.remove(getQuestion(pageIndex, "Question " + sIndex));
        }
        for (Page p : pages) {
            System.out.println(p.name);
            for (Question q : p.questions) {
                System.out.println(q.name);
            }
        }
        parent.getChildren().remove(selectedNode);
        int count = 1;
        Iterator it = parent.getChildren().iterator();
        while (it.hasNext()) {
            MyTreeNode node = (MyTreeNode) it.next();
            if (isPageNode) {
                node.setData("Page " + count++);
            } else {
                node.setData("Question " + count++);
            }
        }
        selectedNode = null;
    }

    private Page getPage(String name) {
        for (Page p : pages) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    private Question getQuestion(int pageIndex, String name) {
        return pages.get(pageIndex).getQuestion(name);
    }

    private void setQuestion(int pageIndex, int questionIndex) {
        Question q = pages.get(pageIndex).questions.get(questionIndex);
        q.type = type;
        q.questionText = questionText;
        q.isRequired = isRequired;
        pages.get(pageIndex).questions.set(questionIndex, q);
    }

    private void getQuestion(int pageIndex, int questionIndex) {
        Question q = pages.get(pageIndex).questions.get(questionIndex);
        type = q.type;
        questionText = q.questionText;
        isRequired = q.isRequired;
    }

    public void clearQuestion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = facesContext.getViewRoot();
        HtmlInputTextarea questionTextComponent = (HtmlInputTextarea) uiViewRoot.findComponent("form:questionText");
        questionTextComponent.setValue("");
        questionText = "";
        //isRequired = false;
        //type = "none";
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public String getRemoveString() {
        return removeString;
    }

    public void setRemoveString(String removeString) {
        this.removeString = removeString;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
        System.out.println("setting question text to " + questionText);
    }

    public boolean isIsRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        System.out.println("setting type to " + type);
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getValidCharacters() {
        return validCharacters;
    }

    public void setValidCharacters(String validCharacters) {
        this.validCharacters = validCharacters;
    }

    public boolean isValidateFormat() {
        return validateFormat;
    }

    public void setValidateFormat(boolean validateFormat) {
        this.validateFormat = validateFormat;
    }

    public boolean isValidateContents() {
        return validateContents;
    }

    public void setValidateContents(boolean validateContents) {
        this.validateContents = validateContents;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
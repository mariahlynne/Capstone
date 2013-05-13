
import java.io.Serializable;

public class Question implements Serializable {
    
    public String name;
    public int questionIndex;
    public int pageIndex;
    
    //Attributes must also be added to the backing bean
    public String questionText;
    public boolean isRequired = false;
    public String errorMessage = "This question is required.";
    public String toolTip;
    public String type = "none";
    public int minLength;//textbox
    public int maxLength;//textbox
    public String validCharacters;//textbox
    public boolean validateFormat;//textbox
    public boolean validateContents;//textbox   
    
    public Question(String name, int pageIndex, int questionIndex) {
        this.name = name;
        this.questionIndex = questionIndex;
        this.pageIndex = pageIndex;
    }
}

function test() {
    alert('in test');
    document.getElementById("form\:questionText").value = "yes!";
}

function showPage() {
    hideAll();
    $("#questionTextSection").hide();
    $("#requiredCheckboxSection").hide();
    $("#questionTypeSection").hide();
    $("#pageSection").show();
}

function showCorrectQuestionSettings() {
    hideAll();
    $("#questionTextSection").show();
    $("#requiredCheckboxSection").show();
    $("#questionTypeSection").show();
    var type = document.getElementById("form\:questionType").value;
    if (type == "text") {
        $("#textSection").show();
    } else if (type == "wholeNumber") {
        $("#wholeNumberSection").show();
    } else if (type == "decimalNumber") {
        $("#decimalNumberSection").show();
    } else if (type == "date") {
        $("#dateSection").show();
    } else if (type == "multipleChoice") {
        $("#multipleChoiceSection").show();
        $("#multipleChoiceTypeSection").show();
        $("#multipleChoiceAmountSection").show();
        showCorrectMultipleChoice();
    }
}

function hideAll() {
    $("#pageSection").hide();
    $("#requiredSection").hide();
    $("#textboxSection").hide();
    $("#textSection").hide();
    $("#wholeNumberSection").hide();
    $("#decimalNumberSection").hide();
    $("#dateSection").hide();
    $("#multipleChoiceSection").hide();
    $("#multipleChoiceTypeSection").hide();
    $("#multipleChoiceOtherParagraphSection").hide();
    $("#multipleChoiceOtherValidateSpecificLength").hide();
    $("#multipleChoiceOtherValidateWholeNumber").hide();
    $("#multipleChoiceOtherValidateDecimalNumber").hide();
    $("#multipleChoiceOtherValidateDate").hide();
    $("#multipleChoiceOtherValidateErrorMessageSection").hide();
    $("#multipleChoiceAmountSection").hide();
    $("#textboxValidateSpecificLength").hide();
    $("#textboxValidateWholeNumber").hide();
    $("#textboxValidateDecimalNumber").hide();
    $("#textboxValidateDate").hide();
    $("#textboxValidateErrorMessageSection").show();
}

function showHideMultipleChoiceFieldOther() {
    if (document.getElementById("multipleChoiceAddOther").checked) {
        $("#multipleChoiceAddOtherFieldSection").show();
    } else {
        $("#multipleChoiceAddOtherFieldSection").hide();
    }
}

function showHideRequiredSection() {
    if (document.getElementById("isRequired").checked) {
        $("#requiredSection").show();
    } else {
        $("#requiredSection").hide();
    }
}

function showCorrectMultipleChoiceFieldType() {
    $("#multipleChoiceOtherLineSection").hide();
    $("#multipleChoiceOtherParagraphSection").hide();
    var type = document.getElementById('form\:multipleChoiceOtherFieldType').value;
    if (type == "line") {
        $("#multipleChoiceOtherLineSection").show();
    } else {
        $("#multipleChoiceOtherParagraphSection").show();
    }
}

function moveChoiceUp() {
    var selected = $("#multipleChoiceChoices option:selected");
    var text = selected.text();
    var index = $("#multipleChoiceChoices option").index(selected);
    if (index != 0) {
        selected.remove();
        $("#multipleChoiceChoices option:eq(" + (index - 1) + ")").before("<option value='1'>" + text + "</option>");
        $('#multipleChoiceChoices option')[index - 1].selected = true; 
    }
    $('#multipleChoiceChoices').focus();
}

function moveChoiceDown() {
    var selected = $("#multipleChoiceChoices option:selected");
    var text = selected.text();
    var index = $("#multipleChoiceChoices option").index(selected);
    var size = $("#multipleChoiceChoices option").size();
    if (index != size - 1) { 
        selected.remove();
        $("#multipleChoiceChoices option:eq(" + index + ")").after("<option value='1'>" + text + "</option>");
        $('#multipleChoiceChoices option')[index + 1].selected = true; 
    }
    $('#multipleChoiceChoices').focus();
}

function removeChoice() {
    var index = $("#multipleChoiceChoices option:selected").index();
    var size = $("#multipleChoiceChoices option").size();
    $("#multipleChoiceChoices option:selected").remove();
    if (index == size - 1 && size > 1)
        index--;
    $('#multipleChoiceChoices option')[index].selected = true;     
    $('#multipleChoiceChoices').focus();
}

function addChoice() {
    var choices = document.getElementById('multipleChoiceChoices');
    var newChoice = document.getElementById('form\:multipleChoiceChoice').value;
    choices.options[choices.options.length] = new Option(newChoice, '1');
    document.getElementById('form\:multipleChoiceChoice').value = "";
    document.getElementById('form\:multipleChoiceChoice').focus();
}

function showCorrectMultipleChoiceValidationOptions() {
    $("#multipleChoiceOtherValidateSpecificLength").hide();
    $("#multipleChoiceOtherValidateWholeNumber").hide();
    $("#multipleChoiceOtherValidateDecimalNumber").hide();
    $("#multipleChoiceOtherValidateDate").hide();
    $("#multipleChoiceOtherValidateErrorMessageSection").show();
    var type = document.getElementById('form\:multipleChoiceOtherValidate').value;
    if (type == "specificLength") {
        $("#multipleChoiceOtherValidateSpecificLength").show();
    } else if (type == "wholeNumber") {
        $("#multipleChoiceOtherValidateWholeNumber").show();
    } else if (type == "decimalNumber") {
        $("#multipleChoiceOtherValidateDecimalNumber").show();
    } else if (type == "date") {
        $("#multipleChoiceOtherValidateDate").show();
    } else {
        $("#multipleChoiceOtherValidateErrorMessageSection").hide();
    }
}

function showCorrectMultipleChoiceAmount() {
    var type = document.getElementById('form\:multipleChoiceAmount').value;
    if (type == "1") {
        document.getElementById('form\:multipleChoiceDisplayType').innerHTML = "<option value=\"radio\">Radio Buttons</option><option value=\"dropdown\">Drop-down List</option>"
    } else {
        document.getElementById('form\:multipleChoiceDisplayType').innerHTML = "<option value=\"dropdown\">Drop-down List</option><option value=\"checkbox\">Checkboxes</option>"
    }
}

function showTextboxValidationOptions() {
    $("#textboxValidateSpecificLength").hide();
    $("#textboxValidateWholeNumber").hide();
    $("#textboxValidateDecimalNumber").hide();
    $("#textboxValidateDate").hide();
    $("#textboxValidateErrorMessageSection").show();
    var type = document.getElementById('form\:textboxValidate').value;
    if (type == "specificLength") {
        $("#textboxValidateSpecificLength").show();
    } else if (type == "wholeNumber") {
        $("#textboxValidateWholeNumber").show();
    } else if (type == "decimalNumber") {
        $("#textboxValidateDecimalNumber").show();
    } else if (type == "date") {
        $("#textboxValidateDate").show();
    } else {
        $("#textboxValidateErrorMessageSection").hide();
    }
}

//
//function showCorrectMultipleChoice() {
//    var type = document.getElementById("multipleChoiceDisplayType");
//    if (type == "radio") {
//        
//    } else if (type == "dropdown") {
//        
//    }
//}
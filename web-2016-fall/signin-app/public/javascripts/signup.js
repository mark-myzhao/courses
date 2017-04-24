$(function() {
  //$(".mes").keyup(function() {
  //  //alert(validator.isFieldValid(this.name, $(this).val()));
  //  if (!validator.isFieldValid(this.name, $(this).val())) {
  //    $(this).parent().find(".warning").val(validator.form[this.name].errorMessage);
  //    $("#submit").attr("disabled", "disabled");
  //  } else {
  //    $(this).parent().find(".warning").val("");
  //    if (validator.isFormValid() && checkRepeatPassword()) {
  //      $("#submit").removeAttr("disabled");
  //    } else {
  //      $("#submit").attr("disabled", "disabled");
  //    }
  //  }
  //});
  $("#repeatPassword").blur(repeatCheck).keyup(repeatCheck);
});

function repeatCheck() {
  if (!checkRepeatPassword()) {
    $(this).parent().find(".warning").val("密码不一致");
  } else {
    $(this).parent().find(".warning").val("");
  }
}

function checkRepeatPassword() {
  return $("#password").val() == $("#repeatPassword").val();
}
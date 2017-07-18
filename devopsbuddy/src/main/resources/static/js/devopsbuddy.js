$(document).ready( main );

function main(){
    $('.btn-collapse').click(function(e){
       e.preventDefault();
       var $this = $(this);
       var $collapse = $this.closest('.collapse-group').find('.collapse');
       $collapse.collapse('toggle');
    });

    /*Contact form validation*/
    $('#contactForm').formValidation({
        framework: 'bootstrap',
        icon:{
            valid:'glyphicon glyphicon-ok',
            invalid:'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields:{
            email:{
                validators:{
                    notEmpty:{
                        message:'The email is required'
                    },
                    emailAddress:{
                        message: 'The input is not a valid email address'
                    }
                }
            },
            firstName:{
                validators:{
                    notEmpty:{
                        message: 'The First Name is required.'
                    }
                }
            },
            lastName:{
                validators:{
                    notEmpty:{
                        message:'The Last Name is required.'
                    }
                }
            },
            feedback:{
                validators:{
                    notEmpty:{
                        message:'Your feedback is valued and required'
                    }
                }
            }
        }
    });

    /*Save Password form validation*/
    $('#savePasswordForm').formValidation({
        framework: 'bootstrap',
        icon:{
            valid:'glyphicon glyphicon-ok',
            invalid:'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields:{
            password:{
                validators:{
                    notEmpty:{
                        message: 'The password is required'
                    },
                    identical:{
                        field: 'confirmPassword',
                        message: 'The password and its confirmation is not the same.'
                    }
                }
            },
            confirmPassword:{
                validators:{
                    notEmpty:{
                        message:'The confirmation password is required.'
                    },
                    identical:{
                        field:'password',
                        message: 'The password and its confirmation is not the same.'
                    }
                }
            }
        }
    });

    /*forgotPassword form validation*/
    $('#forgotPasswordForm').formValidation({
       framework: 'bootstrap',
        icon:{
            valid:'glyphicon glyphicon-ok',
            invalid:'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields:{
            email:{
                validators:{
                    notEmpty:{
                        message: 'Email is required.'
                    },
                    emailAddress:{
                        message:'The input is not a valid email address.'
                    }
                }
            }
        }
    });

    /*login Form validation*/
    $('#loginForm').formValidation({
       framework: 'bootstrap',
        icon:{
            valid:'glyphicon glyphicon-ok',
            invalid:'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields:{
            username: {
                validators:{
                    notEmpty:{
                        message:'The user name is required.'
                    }
                }
            },
            password:{
                validators:{
                    notEmpty:{
                        message: 'The password is required.'
                    }
                }
            }
        }
    });

}
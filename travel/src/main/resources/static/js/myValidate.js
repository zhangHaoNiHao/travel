$().ready(function() {
	
// 自定义验证规则
$.validator.addMethod("email", function(value, element) {   
			var emailReg = /.*@.*\.?.*?\.(com|cn)$/;
			return this.optional(element) || (emailReg.test(value));
		}, "请正确填写您的邮箱");

$.validator.addMethod("number", function(value, element) {   
	var numberReg = /^[1][3458][0-9]{9}$/;
	return this.optional(element) || (numberReg.test(value));
}, "请正确填写您的手机号码");

$.validator.addMethod("usernp", function(value, element) {   
	var usernameReg = /^[a-zA-Z0-9_]+$/;
	return this.optional(element) || (usernameReg.test(value));
}, "请正确填写您的用户名/密码");

$.validator.addMethod("code", function(value, element) {   
	var codeReg = /^[0-9A-Z]{4}$/;
	return this.optional(element) || (cardReg.test(value));
}, "请正确填写验证码");

$.validator.addMethod("cardid", function(value, element) {   
	var cardReg = /^[0-9]{17}(X|x|[0-9])$/;
	return this.optional(element) || (cardReg.test(value));
}, "请正确填写您的用户名/密码");

//规则（把XXX改为相应的表单id）
  $("#checkForm").validate({
	  errorElement : 'span',  
      errorClass : 'help-block',  
      focusInvalid : false,   
	  rules: {
		  	email:{
		  		required:true,
		  		email:true
		  	},
		  	cardid:{
		  		required:true,
		  		cardid:true
		  	},
	    	depart:{
	    		required:true
	    	},
	    	truename:{
	    		required:true
	    	},
	    	director:{
	    		required:true
	    	},
	    	hangye:{
	    		required:true
	    	},
		  	old:{
		  		required : true,
		  		usernp :true
		  	},
			password2:{
				required : true,
				equalTo:"#password"
			},
			Pnumber:{
				required : true,
				number : true,
			},
			username:{
				required : true,
				usernp:true,
				minlength:4
			},
			password:{
				required : true,
				usernp:true,
				minlength:6
			},
			codeCheck:{
				required :true,
				code : true,
				equalTo:"#checkCode"
			}
	    },
	    messages: {
	    	
	    	cardid:{
		  		required:"身份证号不能为空",
		  		cardid:"身份证号错误，身份证号必须18位"
		  	},
	    	director:{
	    		required:"真实姓名不能为空"
	    	},
	    	email:{
	    		required:"邮箱不能为空",
		  		email:"邮箱格式错误，格式为XXX@XX.XXX"
	    	},
	    	depart:{
	    		required:"工作单位不能为空"
	    	},
	    	director:{
	    		required:"专业方向不能为空"
	    	},
	    	hangye:{
	    		required:"所在行业不能为空"
	    	},
			old:{
				required : "旧密码为空",
				usernp:"密码格式不正确，至少6位数字/字母/下划线"
			},
	    	username:{
				required : "用户名为空",
				usernp:"用户名格式不正确，必须是数字/字母/下划线",
				minlength:"用户名至少4位数字/字母/下划线"
			},
			Pnumber:{
				required : "手机号为空",
				number : "手机号错误，11位数字"
			},
			password:{
				required : "密码为空",
				usernp:"密码格式不正确，至少6位数字/字母/下划线",
				minlength:"至少6位数字/字母/下划线"
			},
			password2:{
				required : "确认密码为空",
				equalTo:"两次输入不一致"
			},
			codeCheck :{
				required:"请输入验证码",
					code:"验证码格式错误，必须是数字或字母",	
				equalTo	:"验证码输入错误"
			}
			
	    },  
	    highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },  
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },  
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        }
        
	});
});
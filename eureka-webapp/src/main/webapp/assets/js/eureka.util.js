// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.util = new function () {
	var messages = {
		500: "An internal error has occured. Please contact the technical team for further assistance."
	};
	this.insertMailToTag = function (container, userName, domainName) {
		var atSign = "&#64;"
		var email = userName + atSign + domainName;
		var content = "<a href=\"mail" + "to:" + email + "\">" + email + "</a>";
		$(container).html(content);
	};
	this.getUserMessage = function (status,content) {
		switch(status){
			case 500:
				return messages[status];
			default:
				return content;
			}
	};
};

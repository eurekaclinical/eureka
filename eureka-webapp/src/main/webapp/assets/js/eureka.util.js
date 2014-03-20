// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.util = new function () {
	this.insertMailToTag = function (container, userName, domainName) {
		var atSign = "&#64;"
		var email = userName + atSign + domainName;
		var content = "<a href=\"mail" + "to:" + email + "\">" + email + "</a>";
		$(container).html(content);
	};
};

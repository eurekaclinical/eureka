// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

window.eureka.index = new function () {
	var self = this;
	
	self.writeSupport = function() {
		$.getJSON("assets/data/supported_by.json", function(data) {
			$("#support").html(function() {
				var supportedBy = data.supportedBy;
				var result = supportedBy.slice(0, -1).join("; ");
				if (supportedBy.length > 1) {
					result += "; and ";
				}
				result += supportedBy[supportedBy.length - 1];
				return result;
			});
		});
	};
	
	self.writeVersionHistory = function() {
		$.getJSON("assets/data/version_history.json", function(data) {
			var versionHistory = data.versionHistory;
			if (versionHistory && versionHistory.length > 0) {
				$("#versionHistory").html(function () {
					var result = "";
					var i = 0;
					function appendVersion() {
						result += '<div class="col-sm-2">';
						result += versionHistory[i].releaseDate;
						result += '</div><div class="col-sm-10">';
						result += versionHistory[i].description;
						result += '</div></div>';
					}
					result += '<div class="row">';
					appendVersion();
					for (i = 1; i < versionHistory.length; i++) {
						result += '<div class="row vert-offset">';
						appendVersion();
					}
					return result;
				});
			}
		});
	};
};

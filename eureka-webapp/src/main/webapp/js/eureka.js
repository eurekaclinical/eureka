/* Eureka WebApp. Copyright (C) 2012 Emory University. Licensed under http://www.apache.org/licenses/LICENSE-2.0. */

var droppedElements  = new Object();

// Starting to "namespace" some functions to reduce clutter in the global namespace
var eureka = new Object();
eureka.util = new Object();
eureka.util.objSize = function (obj) {
	var size = 0;
	for (key in obj) {
		if (obj.hasOwnProperty(key)) {
			size++;
		}
	}
	return size;
};
eureka.util.getIn = function (obj, path) {
	var current = obj;
	for (var i = 0; i < path.length; i++) {
		current  = current[path[i]];
		if (!current) {
			break;
		}
	}
	return current;
};
eureka.util.setIn = function (obj, path, value) {
	var current = obj;
	for (var i = 0; i < path.length - 1; i++) {
		var tmp = current[path[i]];
		if (!tmp) {
			tmp = new Object();
			current[path[i]] = tmp;
		}
		current = tmp;
	}
	current[path[path.length - 1]] = value;
};
eureka.util.removeIn = function (obj, path) {
	var current = obj;
	for (var i = 0; i < path.length - 1; i++) {
		current = current[path[i]];
		if (current == null) {
			break;
		}
	}
	delete current[path[path.length - 1]];
};

eureka.trees = {
	init: function(dndActions, deleteActions) {
		var tabContainers = $('div.tabs > div');
		tabContainers.hide().filter(':first').show();

		$('div.tabs ul.tabNavigation a').click(function () {
			tabContainers.hide();
			tabContainers.filter(this.hash).show();
			$('div.tabs ul.tabNavigation a').removeClass('selected');
			$(this).addClass('selected');
			return false;
		}).filter(':first').click();
		
		$("#systemTree").jstree({
			"json_data" : {
				"ajax" : {
					"url" : "systemlist" ,
					"data": function(n) {
						return {
							key : n.attr ? n.attr("data-key") : "root"
						};
					}

				}
			},
			"crrm" : {
				// prevent movement and reordering of nodes
				"move" : {
					"check_move" : function (m) {
						return false;
					}
				}
			},
			"dnd" : {
				"drop_finish": function(data) {
					eureka.trees.dropFinishCallback(data, dndActions, deleteActions)
				},
				"drop_check": function (data) {
					var target = data.r;
					var sortable = $(target).find('ul.sortable');
					var datatype = $(sortable).data("proptype");
					var droppable = false;

					if (datatype == "empty" || datatype == $(data.o).data("type") || $(sortable).data('drop-type') === 'single') {
						droppable = true;
					}

					return droppable;
				}
			},
			// search disabled until we figure out a way to search for nodes not currently loaded in the tree
				//"search" : {
					//"show_only_matches" : true,
					//},
			"plugins" : [ "themes", "json_data", "ui", "crrm", "dnd"/*, "search"*/ ]
		});

		$("#userTree").jstree({
			"json_data" : {
				"ajax" : {
					"url" : "userproplist?key=root"
				}
			},
			"dnd" : {
				"drop_finish" : function(data) {
					eureka.trees.dropFinishCallback(data, dndActions, deleteActions)
				}
			},
			"plugins" : [ "themes", "json_data", "ui", "dnd" ]
		});
	},

	addDroppedElement: function(propType, dropped, dropTarget) {
		var elementKey = $(dropped).data('key');
		var sourceId = $(dropTarget).data('count');
		var sourcePath = [propType, elementKey, 'sources', sourceId];
		var defPath = [propType, elementKey, 'definition'];
		var definition = eureka.util.getIn(droppedElements, defPath);

		eureka.util.setIn(droppedElements, sourcePath, dropTarget);
		if (!definition) {
			var properties = ['key', 'desc', 'type', 'subtype', 'space'];
			definition = new Object();
			$.each(properties, function(i, property) {
				definition[property] = $(dropped).data(property);
			});
			eureka.util.setIn(droppedElements, defPath, definition);
		}

		var allSourcesPath = [propType, elementKey, 'sources'];
		var allSources = eureka.util.getIn(droppedElements, allSourcesPath);
		var size = eureka.util.objSize(allSources);
		if (size > 1) {
			for (key in allSources) {
				if (allSources.hasOwnProperty(key)) {
					var source = allSources[key];
					var items = $(source).find('li');
					$(items).each(function (i, item) {
						var span = $(item).find('span.desc');
						var newText = $(dropped).data('desc') + ' [' + $(source).data('count') + ']';
						$(span).text(newText);
					});
				}
			}
		}
	},

	removeDroppedElement: function(propType, removed, removeTarget) {
		var elementKey = $(removed).data('key');
		var sourceId = $(removeTarget).data('count');
		var path = [propType, elementKey, 'sources', sourceId];
		eureka.util.removeIn(droppedElements, path);

		var allSourcesPath = [propType, elementKey, 'sources'];
		var allSources = eureka.util.getIn(droppedElements, allSourcesPath);
		var size = eureka.util.objSize(allSources);
		if (size <= 1) {
			for (key in allSources) {
				if (allSources.hasOwnProperty(key)) {
					var source = allSources[key];
					var items = $(source).find('li');
					$(items).each(function (i, item) {
						var span = $(item).find('span.desc');
						var newText = $(removed).data('desc');
						$(span).text(newText);
					});
				}
			}
		}
	},

	idIsNotInList: function(target, id) {
		var retVal = true;
		$(target).find('ul.sortable').find('li').each( function(i, item) {
			if ($(item).data('key') == id) {
				retVal = false;
			}

		});
		return retVal;
	},

	dropFinishCallback: function(data, dndActions, deleteActions) {
		var propType = $("input:radio[name='type']:checked").val();
		var target = data.e.currentTarget;
		var textContent = data.o[0].children[1].childNodes[1].textContent;

		if (eureka.trees.idIsNotInList(target, data.o[0].id)) {

			var infoLabel = $(target).find('div.label-info');
			infoLabel.hide();

			var sortable = $(target).find('ul.sortable');
			var newItem = $('<li></li>')
			.attr("data-space", $(data.o[0]).data("space"))
			.attr("data-desc", textContent)
			.attr("data-type", $(data.o[0]).data("type"))
			.attr("data-subtype", $(data.o[0]).data("subtype") || '')
			.attr('data-key', $(data.o[0]).data("proposition") || $(data.o[0]).data('key'));

			

			// check that all types in the categorization are the same
			if ($(sortable).data('drop-type') === 'multiple' && $(sortable).data("proptype") !== 'empty') {
				if ($(sortable).data("proptype") !== $(newItem).data("type")) {
					return;
				}
			} else {
				var tmptype = $(newItem).data("type");
				$(sortable).data("proptype", tmptype);
			}

			/*var X = $("<span></span>", {
				'class': "delete"
			});
			eureka.trees.attachDeleteAction(X, deleteActions);

			var txt = $("<span></span>", {
				'class': 'desc',
				'text': textContent
			});*/
			//this loop is executed only during replacement of a system element when droptype==single. In all other cases(adding element to multiple droptype lists, adding a new element to an empty list) the else statement is executed. 
			if ($(sortable).data('drop-type') === 'single' && $(sortable).find('li').length>0) {
				
				//$(sortable).find('li').each(function (i,item) {
					//$(item).find('span.delete').click();
					
					var toRemove = $(sortable).find('li');
					var $target = $(sortable).parent();
					var dialog = $('<div></div>');
					$(dialog).dialog({
						'title': 'Remove Data Element',
						'modal': true,
						'resizable': false,
						'buttons': {
							"Confirm": function() {
							
								eureka.trees.deleteItem(toRemove,sortable,deleteActions,1);
								
								eureka.trees.addNewItemToList(data,sortable,newItem,deleteActions,dndActions);

								$(this).dialog("close");
								$(this).remove();
							},
							"Cancel": function() {
								$(this).dialog("close");
								$(this).remove();
							}
						}
					});
					$(dialog).html('Are you sure you want to remove data element ' + toRemove.text() + '?');
					$(dialog).dialog("open");
					//$(item).remove();
			}
			else
			{
			
				eureka.trees.addNewItemToList(data,sortable,newItem,deleteActions,dndActions);	
			
		}
	}
},
deleteItem:function(toRemove,sortable,deleteActions,replace){
				var infoLabel = sortable.siblings('div.label-info');
				var type = $("input:radio[name='type']:checked").val();
				var target = sortable.parent();
				eureka.trees.removeDroppedElement(type, toRemove,sortable);
				eureka.trees.setPropositionSelects(sortable.closest('[data-definition-container="true"]'));
				toRemove.remove();
				if (sortable.find('li').length == 0 && replace==0) {
						sortable.data('proptype','empty');
						infoLabel.show();
					}

				// remove the properties from the drop down
				$('select[data-properties-provider=' + $(target).attr('id') + ']').each(function (i, item) {
					$(item).empty();
				});
				//remove the disabled attribute to the property textbox if any.
				var parentTable = $(target).parent().parent().parent();
				var inputProperty=$(parentTable).find('input.propertyValueField');
				$(inputProperty).removeAttr('disabled');
				var inputPropertycheckbox=$(parentTable).find('input.propertyValueConstraint');
				$(inputPropertycheckbox).removeAttr('disabled');

				// perform any additional delete actions
				if (deleteActions && deleteActions[type]) {
					deleteActions[type]();
				}
},
addNewItemToList:function(data,sortable,newItem,deleteActions,dndActions){
	var target = $(sortable).parent();
	var propType = $("input:radio[name='type']:checked").val();
	
	var X = $("<span></span>", {
		'class': "delete"
	});
	eureka.trees.attachDeleteAction(X, deleteActions);
	
	newItem.append(X);
	newItem.append(data.o[0].children[1].childNodes[1].textContent);
	sortable.append(newItem);
	
	// set the properties in the properties select
	if ($(data.o[0]).data('properties')) {
	var properties = $(data.o[0]).data('properties').split(",");
	$('select[data-properties-provider=' + $(target).attr('id') + ']').each(function (i, item) {
	$(item).empty();
	$(properties).each(function (j, property) {
	$(item).append($('<option></option>').attr('value', property).text(property));
	});
	});
	}
	else 
	{
		var parentTable = $(target).parent().parent().parent();
		var inputProperty=$(parentTable).find('input.propertyValueField');
		$(inputProperty).attr('disabled','disabled');
		var inputPropertycheckbox=$(parentTable).find('input.propertyValueConstraint');
		$(inputPropertycheckbox).attr('disabled','disabled');
	}

	// add the newly dropped element to the set of dropped elements
	eureka.trees.addDroppedElement(propType, newItem, sortable);
	eureka.trees.setPropositionSelects($(sortable).closest('[data-definition-container="true"]'));

// finally, call any actions specific to the type of proposition being entered/edited
if (dndActions && dndActions[propType]) {
	dndActions[propType](data.o[0]);
}
},
	setPropositionSelects: function(elem) {
		var type = $("input:radio[name='type']:checked").val();
		var droppedElems = droppedElements[type];
		var selects = $(elem).find('select[name="propositionSelect"]');
		$(selects).each(function (i, sel) {
			var $sortable = $(sel).closest('.drop-parent').find('ul.sortable');
			var originalSource = $(sel).data('sourceid');
			$(sel).attr('data-sourceid','');
			$(sel).empty();
			$.each(droppedElems, function(elemKey, elemValue) {
				var sources = droppedElems[elemKey]['sources'];
				$.each(sources, function(sourceKey, sourceValue) {
					var $items = $(sourceValue).find('li');
					var selectedItem;
					$items.each(function(i, item) {
						if ($(item).data('key') == elemKey) {
							selectedItem = item;
						}
						if (selectedItem && $sortable.data('count') != $(sourceValue).data('count')) {
							var sourceId = $(sourceValue).data('count');
							var value = $(selectedItem).data('key') + '__' + sourceId;
							var desc = $(selectedItem).data('desc');
							if (eureka.util.objSize(sources) > 1) {
								desc += ' [' + sourceKey + ']';
							}
							var opt = $('<option></option>', {
								'value': value
							}).text(desc);
							if (value == $(selectedItem).data('key') + '__' + originalSource) {
								opt.attr('selected','selected');
							}
							$(sel).append(opt);
						}
					});
				});
			});
		});
	},

	attachDeleteAction: function(elem, deleteActions) {
		$(elem).each(function(i, item) {
			$(item).click(function () {
				var $toRemove = $(item).closest('li');
				var $sortable = $toRemove.closest('ul.sortable');
				var $infoLabel = $sortable.siblings('div.label-info');
				var $target = $sortable.parent();
				var dialog = $('<div></div>');
				$(dialog).dialog({
					'title': 'Remove Data Element',
					'modal': true,
					'resizable': false,
					'buttons': {
						"Confirm": function() {
						eureka.trees.deleteItem($toRemove,$sortable,deleteActions,0);
							$(this).dialog("close");
							$(this).remove();
						},
						"Cancel": function() {
							$(this).dialog("close");
							$(this).remove();
						}
					}
				});
				$(dialog).html('Are you sure you want to remove data element ' + $toRemove.text() + '?');
				$(dialog).dialog("open");
			});
		});
	}
};

$(document).ready(function() {
	
	var fileUploadErrors = {
		maxFileSize: 'File is too big',
		minFileSize: 'File is too small',
		acceptFileTypes: 'Filetype not allowed',
		maxNumberOfFiles: 'Max number of files exceeded',
		uploadedBytes: 'Uploaded bytes exceed file size',
		emptyResult: 'Empty file upload result'
	};
	
	$.validator.addMethod("passwordCheck",
		function(value, element) {
			return value.length>= 8 && /[0-9]+/.test(value) && /[a-zA-Z]+/.test(value);
		},
		"Please enter at least 8 characters with at least 1 digit."
		);
	
	if ($("#editUserForm").length > 0){
		var editUserFormValidator = $("#editUserForm").validate({
			rules: {
				role: "required"
			},
			messages: {
				role: "Select a Role"
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
				
	}
	
	if ($("#signupForm").length > 0){


		var signupFormValidator = $("#signupForm").validate({
			rules: {
				firstName: "required",
				lastName: "required",
				password: {
					required: true,
					minlength: 8,
					passwordCheck: true
				},
				organization: {
					required: true
				},
				verifyPassword: {
					required: true,
					minlength: 8,
					equalTo: "#password"
				},
				email: {
					required: true,
					email: true,
				},
				verifyEmail: {
					required: true,
					email: true,
					equalTo: "#email"
				},
				agreement: {
					required: true
				},
				title: {
					required: true
				},
				department: {
					required: true
				}
			},
			messages: {
				firstName: "Enter your firstname",
				lastName: "Enter your lastname",
				organization: "Enter your organization",
				password: {
					required: "Provide a password",
					rangelength: jQuery.format("Please enter at least {0} characters")
				},
				verifyPassword: {
					required: "Repeat your password",
					minlength: jQuery.format("Please enter at least {0} characters"),
					equalTo: "Enter the same password as above"
				},
				email: {
					required: "Please enter a valid email address",
					minlength: "Please enter a valid email address",
				},
				verifyEmail: {
					required: "Please enter a valid email address",
					minlength: "Please enter a valid email address",
					equalTo: "Enter the same email as above"					
				},
				agreement: {
					required: "Please check the agreement checkbox"
				},
				title: "Enter your title",
				department: "Enter your department"
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// specifying a submitHandler prevents the default submit, good for the demo
			//submitHandler: function() {
			//	alert("submitted!");
			//},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
		
		
	}

	
	$('#newPasswordTable').hide();
	$('#saveAcctBtn').hide();
	$('#registrationComplete').hide();
	$('#passwordChangeComplete').hide();
	
	$('#editAcctBtn').click(function(){
		$('#newPasswordTable').show();
		$('#editAcctBtn').hide();
		$('#saveAcctBtn').show();
	});

	$('#ChangePasswordbtn').click(function(e){
		e.preventDefault();
		$('#newPasswordTable').show();
		$('#editAcctBtn').hide();
		$('#saveAcctBtn').show();
	});
	
	$('#ChangePasswordbtn').click(function(){
		$('#newPasswordTable').show();
		$('#editAcctBtn').hide();
		$('#saveAcctBtn').show();
		return false;
	});
	
	$("#signupForm").submit(function() {
		if (signupFormValidator.valid()) {
			var firstName = $('#firstName').val();
			var lastName  = $('#lastName').val(); 
			var organization = $('#organization').val();
			var password = $('#password').val();
			var verifyPassword = $('#verifyPassword').val();
			var  email = $('#email').val();
			var verifyEmail = $('#verifyEmail').val();
			var title = $('#title').val();
			var department = $('#department').val();
	
			var dataString = 'firstName='+ firstName+ '&lastName=' + lastName + '&organization=' + organization + 		
			'&password=' + password+ '&verifyPassword=' + verifyPassword + 
			'&email=' + email + '&verifyEmail=' + verifyEmail + '&title=' + title + '&department=' + department;		
			$.ajax({
				type: 'POST',
				url: 'register',
				data: dataString,
				success: function() {
					$('#passwordChangeFailure').hide();
					$('#signupForm').hide();
					$('#registerHeading').hide();
					$('#registrationComplete').show();

				}, 
				error: function(xhr, err) {

					$('#passwordChangeFailure').show();
					$('#passwordErrorMessage').text(xhr.responseText)

				} 

			});
		}

		return false;
		

	});
	 
	 
	if ($("#userAcctForm").length > 0){
		var userFormValidator = $("#userAcctForm").validate({
			rules: {
				oldPassword: {
					required: true
				},
				newPassword: {
					required: true,
					minlength: 8,
					passwordCheck: true
				},
				verifyPassword: {
					required: true,
					minlength: 8,
					equalTo: "#newPassword"
				}
			},
			messages: {
				oldPassword: "Please enter your old password",
				newPassword: {
					required: "Provide a password",
					rangelength: jQuery.format("Please enter at least {0} characters")
				},
				verifyPassword: {
					required: "Repeat your password",
					minlength: jQuery.format("Please enter at least {0} characters"),
					equalTo: "Enter the same password as above"
				}
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// specifying a submitHandler prevents the default submit, good for the demo
			//submitHandler: function() {
			//	alert("submitted!");
			//},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
		 
	}
	 
	$("#ResetPsdForm").submit(function(){
		var email = $('#email').val();
		var dataString = 'email='+ email;
		$.ajax({
			type: 'POST',
			url: 'forgot_password',
			data: dataString,
			success: function() {
				$('#passwordresetComplete').show();
				$('#passwordresetComplete').text("Password has been reset.You will receive an email with the new password.");
				$('#saveAcctBtn').hide();

			}, 
			error: function(xhr, status, error) {

				$('#passwordresetComplete').show();
				$('#passwordresetComplete').text(xhr.responseText);

			} 
		});
		return false;
	});
	 
	if ($("#passwordExpirationfrm").length > 0){
		var pwExpFormValidator = $("#passwordExpirationfrm").validate({
			rules: {
				oldExpPassword: {
					required: true
				},
				newExpPassword: {
					required: true,
					minlength: 8,
					passwordCheck: true
				},
				verifyExpPassword: {
					required: true,
					minlength: 8,
					equalTo: "#newExpPassword"
				}
			},
			messages: {
				oldExpPassword: "Please enter your old password",
				newExpPassword: {
					required: "Provide a password",
					rangelength: jQuery.format("Please enter at least {0} characters")
				},
				verifyExpPassword: {
					required: "Repeat your password",
					minlength: jQuery.format("Please enter at least {0} characters"),
					equalTo: "Enter the same password as above"
				}
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// specifying a submitHandler prevents the default submit, good for the demo
			//submitHandler: function() {
			//	alert("submitted!");
			//},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
		 
	}
	 
	$("#passwordExpirationfrm").submit(function(){
		var oldPassword = $('#oldExpPassword').val();
		var newPassword = $('#newExpPassword').val();
		var verifyPassword = $('#verifyExpPassword').val();
		var userId = '';
		var targetURL=$('#targetURL').val();
			
		var dataString = 'action=save' +
		'&id='+ userId +
		'&oldPassword=' + oldPassword + 
		'&newPassword=' + newPassword + 
		'&verifyPassword=' + verifyPassword;  
	        	
		if (pwExpFormValidator.valid()) {   
			$.ajax({
				type: 'POST',
				url: 'user_acct',
				data: dataString,
				success: function() {
					window.location.href = targetURL;

				}, 
				error: function(xhr, status, error) {

					$('#passwordChangeComplete').show();
					$('#passwordChangeComplete').text(xhr.responseText);
					$('#passwordChangeComplete').css({
						'font-weight' : 'bold',
						'font-size': 16
					});

				} 
			});
	        
		}
		return false;
	});
	 
	 
	$("#userAcctForm").submit(function(){
		var oldPassword = $('#oldPassword').val();
		var newPassword = $('#newPassword').val();
		var verifyPassword = $('#verifyPassword').val();
		var userId = $('#id').val();
			
		var dataString = 'action=save' +
		'&id='+ userId +
		'&oldPassword=' + oldPassword + 
		'&newPassword=' + newPassword + 
		'&verifyPassword=' + verifyPassword;  
	        
		if (userFormValidator.valid()) {                   		
			$.ajax({
				type: 'POST',
				url: 'user_acct',
				data: dataString,
				success: function() {
					$('#passwordChangeComplete').show();
					$('#passwordChangeComplete').text("Password has been changed.");
					$('#newPasswordTable').hide(); 
					$('#passwordExpirationMsg').hide();

				}, 
				error: function(xhr, status, error) {

					$('#passwordChangeComplete').show();
					$('#passwordChangeComplete').text(xhr.responseText);

				} 
			});
		}

		return false;
	});
	 
	if ($("form#uploadForm").length > 0){
		// Helper functions
		function updateSubmitButtonStatus() {
			var doDisable = false;
			if ($('form#uploadForm').data('job-running')) {
				doDisable = true;
			} else {
				var missingRequired = false;
				$(".browseButton").each(function() {
					if (!$(this).prop('disabled') && $(this).data('required') && !$(this).val()) {
						missingRequired = true;
					}
				});
				if (!missingRequired) {
					doDisable = false;
				} else {
					doDisable = true;
				}
			}
			$('input:submit').prop('disabled', doDisable);
		}
		
		function updateInputFields(sourceId) {
			$(".uploads").each(function(i, r) {
				if ($(r).attr('id') === 'uploads' + sourceId) {
					$(r).find("input[type='file']").prop('disabled', false);
					$(r).show();
				} else {
					$(r).find("input[type='file']").prop('disabled', true);
					$(r).hide();
				}
			});
			updateSubmitButtonStatus();
		}
		
		function onFinish() {
			$('#uploadForm').prop('disabled', false);
			//$('#jobUpload').hide();
			$('form#uploadForm').data('job-running', false)
			updateSubmitButtonStatus();
		}
		
		function setInitialStatus() {
			var running = false;
			if ($('form#uploadForm').data('job-running')) {
				running = true;
			} else {
				//$('#jobUpload').hide();
			}
			var sourceId = $("form#uploadForm").find('select[name="source"]').val();
			updateInputFields(sourceId);
			return running;
		}
		
		eureka.trees.init();
		
		// Initialize widgets
		$("#earliestDate").datepicker();
		$("#latestDate").datepicker();
		
		// Create event handlers.
		var $uploadForm = $("form#uploadForm");
		$uploadForm.find('select[name="source"]').change(
			function() {
				var sourceId = $(this).find(":selected").val();
				updateInputFields(sourceId);
			}
		);
		 
		$('input:file').change(
			function(){
				updateSubmitButtonStatus();
			});
		
		var submitted = false;
		$uploadForm.submit(function() {
			$uploadForm.data('job-running', true)
			var $dataElement = $uploadForm.find('ul[data-type="main"]').find('li').first();
			$("input[name='dateRangeDataElementKey']").val($dataElement.data('key'));
			updateSubmitButtonStatus();
			submitted = true;
		});
		
		
		var running = setInitialStatus();
		
		function poll() {
			if (!submitted) {
				var jobId = $('form#uploadForm').data('jobid');
				$.ajax({
					url : "jobpoll" + (jobId != null ? "?jobId=" + jobId : ""),
					success : function(data) {
						if (!submitted) {
							$('#status').text(data.status);
							$('#startedDate').text(data.startedDateFormatted);
							$('#finishedDate').text(data.finishedDateFormatted);
							$('#messages').text(data.mostRecentMessage);
							if (running && !data.jobSubmitted) {
								onFinish();
								running = false;
							}
						}
					},
					error : function(xhr) {
						if (!submitted) {
							$('#status').text("Job status unavailable");
							$('#startedDate').empty();
							$('#finishedDate').empty();
							$('#messages').empty();
						}
					},
					dataType : "json"
				});
			}
		}
		
		poll();

		(function pollStatus() {
			setTimeout(function() {
				poll();
				pollStatus();	
			}, 5000);
		})();
	}

	if ($("#elements").length > 0) {
		var $dataElements = $("#elements").find('tr.editor-home-data-element');
		$dataElements.each(function(i, dataElement) {
			var viewLink = $(dataElement).find('span.view');
			$(viewLink).click(function() {
				viewElement($(dataElement).data('key'));
			});
			 
			var deleteLink = $(dataElement).find('span.delete');
			$(deleteLink).click(function() {
				deleteElement($(dataElement).data('display-name'), 
					$(dataElement).data('key'));
			});
		});
	}

});
	

// Creates a popup containing a summary of a derived data element.
function viewElement(rowId) {

	//$('<div class="tooltip"><div id="tree"></div></div>').appendTo('body');
	//var rowId = event.target.parentNode.id;
	$('div.tooltip').hide();
	$(".rowdata td").css("background", "white");
	$('#'+rowId + " td").css("background", "yellow");
   
	//positionTooltip(event);
  
	$("#tree").jstree({
		"json_data" : {
			"ajax" : {
				"url" : "userpropchildren?propKey=" + encodeURIComponent(rowId)
				}
		},
		"plugins" : [ "themes", "json_data", "ui" ]
	});

	$('div.tooltip').show();


}

function deleteElement(displayName, key) {
	var $dialog = $('<div></div>')
		.html('Are you sure you want to delete data element ' + 
			displayName + '? You cannot undo this action.')
		.dialog({
			title : "Delete Data Element",
			modal: true,
			resizable: false,
			buttons : {
				"Delete" : function() {
					$.ajax({
						type: 'POST',
						url: 'deleteprop?key=' + encodeURIComponent(key),
						success: function(data) {
							$(this).dialog("close");
							window.location.href="editorhome";
						}, 
						error: function(data, statusCode) {
							var $errorDialog = $('<div></div>')
								.html(data.responseText)
								.dialog({
									title : "Error Deleting Data Element",
									buttons: {
										"OK" : function() {
											$(this).dialog("close");
										}
										
									},
									close: function() {
										$dialog.dialog("close");
									}
								})
							$errorDialog.dialog("open");

						} 
					});
				},
				"Cancel" : function() {
					$(this).dialog("close");
				}
			},
			close : function() {
				window.location.href="editorhome";
			}
	});
	$dialog.dialog("open");
}

function positionTooltip(event){
	var tPosX = event.pageX + 20;
	var tPosY = event.pageY - 150;
	$('div.tooltip').css({
		'position': 'absolute',
		'top': tPosY,
		'left': tPosX,
		'width': '200px',
		'height': '200px',
		'border':'1px solid black',
		'backgroundColor':'#FFFFEE',
	});
}; 

function insertMailToTag(userName, domainName) {
	var atSign = "&#64;"
	var email = userName + atSign + domainName;

	document.write("<a href='mail" + "to:" + email + "'>" + email +"</a>");
}
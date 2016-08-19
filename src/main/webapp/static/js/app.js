;$(function() {
	
	var newIndex, blankItem;
	var strongPassExpr = new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$', '');
	var smthWasEdited = false;
	var formSubmitting = false;

	var showAlertMessage = function(message) {
		alert(message);
	};

	var initLoadMoreProfiles = function() {
		var total = parseInt($('#profileContainer').attr('data-profile-total'));
		if (total <= 1) {
			$('#loadMoreBlock').remove();
		} else {
			$('#loadMoreBtn').click(loadMoreProfiles);
		}
	};
	var loadMoreProfiles = function() {
		var page = parseInt($('#profileContainer').attr('data-profile-number')) + 1;
		var total = parseInt($('#profileContainer').attr('data-profile-total'));
		if (page >= total) {
			$('#loadMoreBlock').remove();
			return;
		};
		var url = '/more-profiles?page=' + page;
		var query = $('#profileContainer').attr('data-search-query');
		if (query !== '') {
			url += '&query=' + query;
		};

		$('#loadMoreContainer').css('display', 'none');
		$('#loadMoreIndicator').css('display', 'block');
		$.ajax({
			url : url,
			success : function(data) {
				$('#loadMoreIndicator').css('display', 'none');
				$('#profileContainer').append(data);
				$('#profileContainer').attr('data-profile-number', page);
				if (page >= total - 1) {
					$('#loadMoreBlock').remove();
				} else {
					$('#loadMoreContainer').css('display', 'block');
				}
			},
			error : function(data) {
				$('#loadMoreIndicator').css('display', 'none');
				showMessage('Error', 'Try again later...');
			}
		});
	};


	var initSubmitFormAjax = function() {
		$('.submit-form-btn').click(function() {
			var btn = $(this);
			var formName = $(this).attr('data-form');
			var form = $('#' + formName);
			var formErrorDiv = $('#' + formName + 'ErrorDiv');
			var formErrorText = $('#' + formName + 'ErrorText');
			var url = form.attr('data-url');
			var csrfToken = $('#csrfToken').val();
			var data = {};
			$('#' + formName + ' input').each(function() {
				var input = $(this);
				var name = input.attr('name');
				var value = input.val();
				data[name] = value;
			});
			$.ajaxSetup({
				headers: {
					'X-Csrf-Token': csrfToken
				}
			});
			$.ajax({
				url : url,
				type: 'POST',
				data: data,
				success : function(data) {
					if (data.status === 'OK') {
						showMessage('Success', data.message);
					} else {
						formErrorText.text(data.message);
						formErrorDiv.removeClass('hidden');
						setTimeout(function() {
							formErrorDiv.addClass('hidden');
						}, 3000);
					};
					
				},
				error : function(data) {
					showMessage('Error', 'Changes not saved...');
				}
			});
		});
	};


	var initShowCertificate = function() {
		$('.modal-certificate-btn').click(showCertificateModal);
	};
	var showCertificateModal = function() {
		var btn = $(this);
		var certificateImage = btn.attr('data-certificate-img');
		var certificateDescription = btn.attr('data-certificate-desc');
		$('#modalCertificateTitle').text(certificateDescription);
		$('#modalCertificateImg').attr('src', certificateImage);
	};

	
	var initEditItem = function() {
		newIndex = parseInt($('#editTable').attr('data-init-size'));
		blankItem = $('#item-' + newIndex);
		setItemIndex(blankItem, newIndex, 'blank');
		blankItem.remove();
	};
	var initAddItem = function() {
		$('#addItem').click(addItem);
	};
	var initRemoveItem = function() {
		$('.remove-item-btn').off('click');
		$('.remove-item-btn').click(removeItem);
	};
	var addItem = function() {
		if (!profileHasAllCategories()) {
			var item = blankItem.clone();
			item.insertBefore($('#addItemRow'));
			setItemIndex(item, 'blank', newIndex);
			initRemoveItem();
			initSkillCategoriesSelection();
			selectSomeCategory(newIndex);
			initSkillCategoriesSelection();
			newIndex++;
		}
	};
	var removeItem = function() {
		var btn = $(this);
		var index = parseInt(btn.attr('data-item'));
		$('#item-' + index).remove();
		initSkillCategoriesSelection();
	};
	var setItemIndex = function(item, oldIndex, newIndex) {
		$('#item-' + oldIndex + ' [name]').each(function() {
			var input = $(this);
			var oldValue = input.attr('name');
			var newValue = oldValue.replace('items[' + oldIndex + '].', 'items[' + newIndex + '].');
			input.attr('name', newValue);
		});
		$('#item-' + oldIndex).attr('id', 'item-' + newIndex);
		$('#closeBtn-' + oldIndex).attr('data-item', newIndex);
		$('#closeBtn-' + oldIndex).attr('id', 'closeBtn-' + newIndex);
	};
	var profileHasAllCategories = function() {
		var currentCategoriesCount = $('.select-category').length;
		var totalCategories = $('.select-category option').length / currentCategoriesCount;
		if (currentCategoriesCount >= totalCategories) {
			return true;
		} else {
			return false;
		}
	};


	var initSkillCategoriesSelection = function() {
		$('.select-category option').removeAttr('disabled');
		$('.select-category').off('change');
		$('.select-category').each(function() {
			var curentCategory = $(this).val();
			$(this).attr('data-curr-cat', curentCategory)
			$('.select-category').not(this)
				.children('option[value="' + curentCategory + '"]')
				.attr('disabled', true);
		});
		$('.select-category').change(function() {
			var curentCategory = $(this).val();
			var prevCategory = $(this).attr('data-curr-cat');
			$(this).attr('data-curr-cat', curentCategory);
			$('.select-category').not(this)
				.children('option[value="' + curentCategory + '"]')
				.attr('disabled', true);
			$('.select-category').not(this)
				.children('option[value="' + prevCategory + '"]')
				.removeAttr('disabled');
		});
	};
	var selectSomeCategory = function(index) {
		$('#item-' + index + ' option').each(function() {
			if ($(this).attr('disabled') !== 'disabled') {
				var value = $(this).val();
				$('#item-' + index + ' .select-category').val(value);
				return;
			}
		});
	};


	var initChoseFile = function() {
		$('#fileButton').click(openChooseFileWindow);
		$('#fileInput').change(setChosenFileName);
	};
	var openChooseFileWindow = function() {
		$('#fileInput').click();
	};
	var setChosenFileName = function() {
		var chosenFileName = $('#fileInput').val().split(/[\\|/]/).pop();
		$('#fileName').html(chosenFileName);
	};


	var initHobbyButtons = function() {
		$('.hobby-btn').click(switchHobbyStatus);
	};
	var switchHobbyStatus = function() {
		var btn = $(this);
		var index = parseInt(btn.attr('data-hobby-ind'));
		var input = $('#hobbyInput-' + index);
		var status = input.val();
		var countInput = $('#currentHobbyCount');
		var count = parseInt(countInput.attr('value'));
		if (status === 'checked') {
			input.val('unchecked');
			btn.removeClass('btn-success');
			btn.addClass('btn-default');
			countInput.attr('value', count - 1);
		};
		if (status === 'unchecked') {
			input.val('checked');
			btn.removeClass('btn-default');
			btn.addClass('btn-success');
			countInput.attr('value', count + 1);
		};
	};


	var initShowMessage = function() {
			var message = $('#modalMessage').attr('data-message');
			if (message !== '') {
				showMessage('Update', message);
			}
	};


	var initSignOutForm = function() {
		$('#signOutBtn').click(function() {
			$('#signOutForm').submit();
		});
	};


	var initRemoveEmptyItemsAndSubmitForm = function() {
		$('#submitListBtn').click(function() {
			$('.remove-item-btn').each(function(index) {
				var oldIndex = parseInt($(this).attr('data-item'));
				var item = $('#item-' + oldIndex);
				setItemIndex(item, oldIndex, index);
			});
			$('#form').submit();
		});
	};


	var initSignUpPopovers = function() {
		$('[data-toggle="popover"]').popover();
	};


	var initPasswordInput = function() {
		$('#passwordInput').on('input', function() {
			var input = $(this);
			var group = $('#passwordGroup');
			var icon = $('#passwordResultIcon');
			var text = $('#passwordResultText');
			var pass = input.val();
			if (pass.length < 3) {
				group.removeClass('has-success has-warning');
				group.addClass('has-error');
				icon.removeClass('hidden glyphicon-ok glyphicon-warning-sign');
				icon.addClass('glyphicon-remove');
				text.removeClass('hidden');
				text.text('Weak password');
			}
			else if (strongPassExpr.test(pass)) {
				group.removeClass('has-warning has-error');
				group.addClass('has-success');
				icon.removeClass('hidden glyphicon-warning-sign glyphicon-remove');
				icon.addClass('glyphicon-ok');
				text.removeClass('hidden');
				text.text('Strong password');
			}
			else {
				group.removeClass('has-success has-error');
				group.addClass('has-warning');
				icon.removeClass('hidden glyphicon-ok glyphicon-remove');
				icon.addClass('glyphicon-warning-sign');
				text.removeClass('hidden');
				text.text('Normal password');
			};
		});
	};


	var smthEdited = function() {
		smthWasEdited = true;
		$('#submitBtn').removeClass('btn-default');
		$('#submitBtn').addClass('btn-info');
		$('#submitListBtn').removeClass('btn-default');
		$('#submitListBtn').addClass('btn-info');
	};
	var submitBtnClicked = function() {
		formSubmitting = true;
	};
	var initAlertAboutUnsavedChanges = function() {
		$('.table input').change(smthEdited);
		$('.table textarea').change(smthEdited);
		$('.table select').change(smthEdited);
		$('.table #addItem').click(smthEdited);
		$('.table .remove-item-btn').click(smthEdited);
		$('.table .hobby-btn').click(smthEdited);
		$('.table #submitBtn').click(submitBtnClicked);
		$(window).on('beforeunload', function(e) {
			if (formSubmitting || !smthWasEdited) {
				return undefined;
			}
			return "It looks like you have been editing something. If you leave before saving, your changes will be lost.";
		});
	};


	var showMessage = function(title, message) {
		$('#messageTitle').text(title);
		$('#messageText').text(message);
		$('#modalMessage').modal('show');
		setTimeout(function() {
			$('#modalMessage').modal('hide');
		}, 3000);
	};
	

	initLoadMoreProfiles();
	initSubmitFormAjax();
	initShowCertificate();
	initEditItem();
	initAddItem();
	initRemoveItem();
	initSkillCategoriesSelection();
	initChoseFile();
	initHobbyButtons();
	initShowMessage();
	initSignOutForm();
	initRemoveEmptyItemsAndSubmitForm();
	initSignUpPopovers();
	initPasswordInput();
	initAlertAboutUnsavedChanges();
});

var onloadCallback = function() {
	var sitekey = $('#g-recaptcha').attr('data-sitekey');
	grecaptcha.render('g-recaptcha', {
		sitekey : sitekey,
		callback : function(response) {
			$('#recaptchaResponse').val(response);
		},
		theme : 'light'
	});
};
;$(function() {
	
	var newIndex, blankItem;

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
	};
	var initAddItem = function() {
		$('#addItem').click(addItem);
	};
	var initRemoveItem = function() {
		$('.remove-item-btn').click(removeItem);
		blankItem.remove();
	};
	var addItem = function() {
		var item = blankItem.clone();
		item.insertBefore($('#addItemRow'));
		setItemIndex(item, 'blank', newIndex);
		newIndex++;
	};
	var removeItem = function() {
		var btn = $(this);
		var index = parseInt(btn.attr('data-item'));
		$('#item-' + index).remove();
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
		$('#closeBtn-' + oldIndex).attr('id', 'close-btn-' + newIndex);
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


	var initSubmitSearchForm = function() {
		$('#searchBtn').click(function() {
			var queryStr = $('#query').val();
			if (queryStr !== '') {
				$('#searchForm').submit();
			} else {
				showMessage('Error', 'Don\'t leave it emty');
			}
		});
	};
	var initSubmitForm = function() {
		$('#submitBtn').click(function() {
			$('#form').submit();
		});
	};
	var initSubmitListForm = function() {
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


	var showMessage = function(title, message) {
		$('#messageTitle').text(title);
		$('#messageText').text(message);
		$('#modalMessage').modal('show');
		setTimeout(function() {
			$('#modalMessage').modal('hide');
		}, 3000);
	};
	

	initLoadMoreProfiles();
	initShowCertificate();
	initEditItem();
	initAddItem();
	initRemoveItem();
	initChoseFile();
	initHobbyButtons();
	initShowMessage();
	initSubmitSearchForm();
	initSubmitForm();
	initSubmitListForm();
	initSignUpPopovers();
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
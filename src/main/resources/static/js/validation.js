$(document).ready(function () {

    const csrfToken = $("meta[name='csrf-token']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");

    $.ajaxSetup({
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        }
    });

    $('#addAddressModal').click(function (event) {
        event.preventDefault();
        const addressForm = {
            firstName: $('#firstName').val(),
            lastName: $('#lastName').val(),
            emailId: $('#emailId').val(),
            country: $('#country').val(),
            streetAddress1: $('#streetAddress1').val(),
            streetAddress2: $('#streetAddress2').val(),
            city: $('#city').val(),
            postalCode: $('#postalCode').val(),
            state: $('#state').val(),
            phoneNumber: $('#phoneNumber').val()
        };

        $.ajax({
            type: "POST",
            url: '/user/addressModal/add',
            data: addressForm,
            success: function (response) {
                if ((response.errorList === null || response.errorList === undefined) && response.address) {
                    const addressId = response.address.id;
                    window.location.href = '/selectAddress/checkout?addressId=' + addressId;
                } else {
                    processErrors(response);
                }
            },
            error: function (ex) {
                console.log(ex);
            }
        });
    });

    $('#addPaymentModal').click(function (event) {
        event.preventDefault();
        const paymentForm = {
            creditCard: $('#creditCard').val(),
            cvv: $('#cvv').val(),
            expirationYear: $('#expirationYear').val(),
            expirationMonth: $('#expirationMonth').val(),
            fullName: $('#fullName').val()
        };

        $.ajax({
            type: "POST",
            url: '/user/paymentModal/add',
            data: paymentForm,
            success: function (response) {
                if ((response.errorList === null || response.errorList === undefined) && response.payment) {
                    const paymentId = response.payment.id;
                    window.location.href = '/selectPayment/checkout?paymentId=' + paymentId;
                } else {
                    processErrors(response);
                }
            },
            error: function (ex) {
                console.log(ex);
            }
        });
    });

    function processErrors(response) {
        // clear form
        $('.is-invalid').removeClass('is-invalid');
        $('.invalid-feedback').remove();
        const errors = response.errorList;
        const dictionary = {};
        // processErrors
        for (let i = 0; i < errors.length; i++) {
            let err = errors[i];
            let field = err.field;
            if (!dictionary[field]) {
                let message = err.defaultMessage;
                $('#' + field).addClass('is-invalid')
                    .after('<span class="invalid-feedback">' + message + '</span>');
                let errorList = [];
                errorList.push(message)
                dictionary[field] = errorList;
            }
        }
    }
});
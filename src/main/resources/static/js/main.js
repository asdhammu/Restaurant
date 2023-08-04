$(document).ready(function () {

    $(".navbar-toggler").click(function () {
        $("#navbarNav").collapse('toggle');
    });

    /*$('.carousel-item:first').addClass("active");*/

    $('.carousel').carousel({
        interval: 2000
    });


    $('#country-drop-down, #country').change(function () {
        var selectedValue = $(this).val();
        $.ajax({
            type: 'GET',
            url: '/states',
            data: {countryName: selectedValue},
            success: function (data) {
                var options = '';
                $.each(data, function (index, option) {
                    options += '<option value="' + option.name + '">' + option.name + '</option>';
                });
                $('#state-drop-down, #state').html(options);
            }
        });
    });

    function searchApi() {
        return {
            source: function (query, response) {
                $.ajax({
                    url: '/product/search',
                    method: 'GET',
                    data: {query: query.term},
                    traditional: true,
                    success: function (data) {
                        console.log(data);
                        response($.map(data, function (value, key) {
                            return {
                                label: value.name,
                                value: value.name,
                                productId: value.productId
                            }
                        }));
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('Error:', errorThrown);
                    }
                });
            },
            minLength: 1,
            select: function (event, ui) {
                window.location.href = '/product/' + ui.item.productId;
                return false;
            }
        };
    }

    $('#searchProductMobile').autocomplete(searchApi()).autocomplete("widget").addClass("mr-autocomplete animate slideIn");
    $('#searchProduct').autocomplete(searchApi()).autocomplete("widget").addClass("mr-autocomplete animate slideIn");
});

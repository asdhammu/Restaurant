$(document).ready(function () {

    $('#cartNavLink, #m-cartNavLink').click(function () {
        $.ajax({
            url: '/shoppingCart',
            method: 'GET',
            success: function (shoppingCart) {
                renderCartCanvas(shoppingCart);
                renderCartMessage(null, null);
                let offCanvas = new bootstrap.Offcanvas(document.getElementById('cartOffCanvas'));
                offCanvas.show();
            },
            error: function (error) {
                console.error(error);
            }
        });
    });

    $('#cart-canvas-input').change(function () {
        let inputValue = $(this).val();
    });
});

function renderCartCanvas(shoppingCart) {
    let cartItemElement = $('#offcanvas-cart-items');
    cartItemElement.empty();
    // render response
    let items = shoppingCart.cartItems;
    let elements = [];
    for (let i = 0; i < items.length; i++) {
        let cartItem = items[i];
        let li = `
                    <li class="list-group-item py-3">
                        <div class="row align-items-center">
                            <div class="col-3">
                                <a href="/product/${cartItem.product.productId}">
                                    <img src="${cartItem.product.imgUrl}" class="img-fluid" alt="">
                                </a>
                            </div>
                            <div class="col">
                                <div class="d-flex mb-4 fw-bold">
                                    <a class="text-body">${cartItem.product.name}</a>
                                    <span class="ms-auto">${shoppingCart.currency}${cartItem.price.price}</span>
                                </div>                                
                                <div class="d-flex align-items-center">
                                    <div class="d-flex">
                                        <button class="btn ps-0" type="submit" onclick="reduceQuantity(event, '${cartItem.product.productId}', '${cartItem.product.name}')"><i class="fa fa-minus"></i></button>                                        
                                        <input onchange="inputChange(event.target.value, '${cartItem.product.productId}', '${cartItem.product.name}')" type="number" min="1" max="99" value="${cartItem.quantity}" class="form-control text-center input-number">                                        
                                        <button class="btn" type="submit" onclick="addQuantity(event,'${cartItem.product.productId}', '${cartItem.product.name}')"><i class="fa fa-plus"></i></button>                                        
                                    </div>                                    
                                    <button class="btn btn-link remove-btn text-gray ms-auto px-0 small-md" type="submit" 
                                        onclick="removeCartItem('${cartItem.product.productId}', '${cartItem.product.name}')">
                                        <i class="fa fa-remove"></i><span class="ps-1">Remove</span>
                                    </button>                                    
                                </div>
                            </div>
                        </div>
                    </li>
                    `
        elements.push(li);
    }
    cartItemElement.html(elements);

    // subtotal
    $('#cart-canvas-subtotal').html(`<strong>${shoppingCart.currency}${shoppingCart.price}</strong>`);

    let count = shoppingCart.count;
    // cart count
    $('#cart-count-canvas').html(`(${count})`)

    $('.cart-count').html(`${count}`);
}

function removeCartItem(productId, productName) {

    let removeMessage = productName + ' was removed';

    $.ajax({
        url: '/shoppingCart',
        method: 'DELETE',
        data: {
            productId: productId
        },
        success: function (shoppingCart) {
            $.ajax({
                url: '/shoppingCart',
                method: 'GET',
                success: function (shoppingCart) {
                    renderCartCanvas(shoppingCart);
                    renderCartMessage(null, removeMessage);
                },
                error: function (error) {
                    console.error(error);
                }
            });
        },
        error: function (error) {
            console.error(error);
        }
    });
}


function renderCartMessage(updateMessage, removeMessage) {
    // clear any existing message
    let cartCanvasMessage = $('#cart-canvas-message');
    cartCanvasMessage.empty();
    
    let htmlMessage = '';
    if (updateMessage !== null && updateMessage !== undefined && updateMessage !== '') {
        htmlMessage = `
        <div class="alert alert-info alert-dismissible fade show">
            <span>${updateMessage}</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        `
    }

    if (removeMessage !== null  && removeMessage !== undefined && removeMessage !== '') {
        htmlMessage = `
        <div class="alert alert-warning alert-dismissible fade show">
            <span>${removeMessage}</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        `
    }

    if (htmlMessage !== '') {
        $("#cart-canvas-message").html(htmlMessage);
    }

}

function inputChange(quantity, productId, productName) {
    updateCartAPI(quantity, productId, productName);
}

function addQuantity(event, productId, productName) {
    let quantity = event.currentTarget.previousElementSibling.value;
    let parsedQuantity = parseInt(quantity, 10) + 1;
    updateCartAPI(parsedQuantity, productId, productName);
}

function reduceQuantity(event, productId, productName) {
    let quantity = event.currentTarget.nextElementSibling.value;
    let parsedQuantity = parseInt(quantity, 10) - 1;
    updateCartAPI(parsedQuantity, productId, productName);
}

function updateCartAPI(quantity, productId, productName) {
    let dataForm = {
        productId: productId,
        quantity: quantity
    };

    let updateMessage;
    let removeMessage;
    if (quantity === 0) {
        removeMessage = productName + ' was removed';
    } else {
        updateMessage = productName + ' quantity updated to ' + quantity;
    }


    $.ajax({
        url: '/shoppingCart',
        method: 'POST',
        data: dataForm,
        success: function (shoppingCart) {
            $.ajax({
                url: '/shoppingCart',
                method: 'GET',
                success: function (shoppingCart) {
                    renderCartMessage(updateMessage, removeMessage);
                    renderCartCanvas(shoppingCart);
                },
                error: function (error) {
                    console.error(error);
                }
            });
        },
        error: function (error) {
            console.error(error);
        }
    });
}
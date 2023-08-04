package my.restaurant.service.impl;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import my.restaurant.dto.PriceDTO;
import my.restaurant.dto.ProductDTO;
import my.restaurant.entity.*;
import my.restaurant.exceptions.CartNotFoundException;
import my.restaurant.exceptions.ProductNotFountException;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.repository.CartItemRepository;
import my.restaurant.repository.CartRepository;
import my.restaurant.repository.ProductRepository;
import my.restaurant.repository.UserRepository;
import my.restaurant.service.AuthenticationFacade;
import my.restaurant.service.CartService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final AuthenticationFacade authenticationFacade;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final HttpSession httpSession;

    public CartServiceImpl(AuthenticationFacade authenticationFacade, CartRepository cartRepository, UserRepository userRepository,
                           ProductRepository productRepository, CartItemRepository cartItemRepository, HttpSession httpSession) {
        this.authenticationFacade = authenticationFacade;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.httpSession = httpSession;
    }

    @Override
    public ShoppingCart getCart() {
        if (authenticationFacade.isLoggedIn()) {
            Cart cart = this.cartRepository.findByUserUsername(this.authenticationFacade.getUsername());
            if (cart == null)
                throw new CartNotFoundException(String.format("No Cart available for user %s", this.authenticationFacade.getUsername()));
            ShoppingCart shoppingCart = new ShoppingCart();
            cart.getCartItems().forEach(x -> {
                Product product = x.getCartItemKey().getProduct();
                ProductDTO productDTO = new ProductDTO(product.getProductId(), product.getProductName(), "/img/product/product1.jpg", product.getDescription(),
                        new PriceDTO(product.getPrice()), null);
                shoppingCart.addItem(productDTO, x.getQuantity(), new PriceDTO(x.getPrice()));
            });
            return shoppingCart;
        } else {
            ShoppingCart shoppingCart = (ShoppingCart) httpSession.getAttribute("cart");
            if (shoppingCart == null) {
                shoppingCart = new ShoppingCart();
                httpSession.setAttribute("cart", shoppingCart);
            }
            return shoppingCart;
        }
    }

    @Override
    public void addItemToCart(ProductDTO productDTO, int quantity) {

        if (authenticationFacade.isLoggedIn()) {
            String username = this.authenticationFacade.getUsername();
            Optional<User> user = this.userRepository.findByUsername(username);

            if (user.isEmpty()) throw new UsernameNotFoundException(String.format("No user for %s", username));

            Optional<Product> productOptional = this.productRepository.getProductByProductId(productDTO.productId());

            if (productOptional.isEmpty())
                throw new ProductNotFountException(String.format("No product for %s", productDTO.productId()));

            Cart cart = this.cartRepository.findByUserUsername(username);
            if (cart == null) throw new RuntimeException("No Cart available");
            CartItem cartItem;
            Optional<CartItem> cartItemOptional = cartItemRepository.findByCartItemKey_CartAndCartItemKey_Product(cart, productOptional.get());
            if (cartItemOptional.isPresent()) {
                cartItem = cartItemOptional.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                cartItem = new CartItem();
                cartItem.setCartItemKey(new CartItemKey(cart, productOptional.get()));
                cartItem.setQuantity(quantity);
                cartItem.setPrice(productDTO.price().price());
                cart.getCartItems().add(cartItem);
            }
            this.cartRepository.save(cart);
        } else {
            ShoppingCart shoppingCart = (ShoppingCart) httpSession.getAttribute("cart");
            if (shoppingCart == null) {
                shoppingCart = new ShoppingCart();
                httpSession.setAttribute("cart", shoppingCart);
            }
            shoppingCart.addItem(productDTO, quantity);
        }


    }

    @Override
    public void removeItem(ProductDTO productDTO) {
        if (this.authenticationFacade.isLoggedIn()) {
            String username = this.authenticationFacade.getUsername();
            Optional<User> user = this.userRepository.findByUsername(username);

            if (user.isEmpty()) throw new UsernameNotFoundException(String.format("No user for %s", username));

            Optional<Product> productOptional = this.productRepository.getProductByProductId(productDTO.productId());

            if (productOptional.isEmpty())
                throw new ProductNotFountException(String.format("No product for %s", productDTO.productId()));

            Cart cart = this.cartRepository.findByUserUsername(username);
            if (cart == null) throw new CartNotFoundException(String.format("No Cart available for user %s", username));

            Optional<CartItem> cartItem = cartItemRepository.findByCartItemKey_CartAndCartItemKey_Product(cart, productOptional.get());
            cartItem.ifPresent(this.cartItemRepository::delete);
        } else {
            ShoppingCart shoppingCart = (ShoppingCart) httpSession.getAttribute("cart");
            shoppingCart.removeItem(productDTO);
        }

    }

    @Override
    public void updateItem(ProductDTO productDTO, int quantity) {
        if (this.authenticationFacade.isLoggedIn()) {
            String username = this.authenticationFacade.getUsername();
            Optional<User> user = this.userRepository.findByUsername(username);

            if (user.isEmpty()) throw new UsernameNotFoundException(String.format("No user for %s", username));

            Optional<Product> productOptional = this.productRepository.getProductByProductId(productDTO.productId());

            if (productOptional.isEmpty())
                throw new ProductNotFountException(String.format("No product for %s", productDTO.productId()));

            Cart cart = this.cartRepository.findByUserUsername(username);
            if (cart == null) throw new CartNotFoundException(String.format("No Cart available for user %s", username));

            Optional<CartItem> cartItemOptional = cartItemRepository.findByCartItemKey_CartAndCartItemKey_Product(cart, productOptional.get());
            if (cartItemOptional.isPresent()) {

                if (quantity < 1) {
                    this.cartItemRepository.delete(cartItemOptional.get());
                } else {
                    CartItem cartItem = cartItemOptional.get();
                    cartItem.setQuantity(quantity);
                    cartItemRepository.save(cartItem);
                }
            }
        } else {
            ShoppingCart shoppingCart = (ShoppingCart) httpSession.getAttribute("cart");
            shoppingCart.updateQuantity(productDTO, quantity);
        }

    }

    @Override
    public void clearShoppingCart() {
        if (authenticationFacade.isLoggedIn()) {
            Cart cart = this.cartRepository.findByUserUsername(this.authenticationFacade.getUsername());
            if (cart == null) throw new CartNotFoundException(String.format("No Cart available for user %s", this.authenticationFacade.getUsername()));
            List<CartItem> cartItems = cartItemRepository.findByCartItemKey_Cart(cart);
            cartItems.forEach(cartItem -> {
                CartItemKey cartItemKey = cartItem.getCartItemKey();
                this.cartItemRepository.deleteCartItem(cartItemKey.getCart().getCartId(), cartItemKey.getProduct().getProductId());
            });
        }else{
            ShoppingCart shoppingCart = (ShoppingCart) httpSession.getAttribute("cart");
            shoppingCart.clear();
        }
    }

    @Override
    public void mergeShoppingCart(ShoppingCart shoppingCart, String username) {
        Cart cart = this.cartRepository.findByUserUsername(username);
        if (cart == null) throw new CartNotFoundException(String.format("No Cart available for user %s", username));
        shoppingCart.getCartItems().forEach(sessionCartItem -> {
            Optional<Product> productOptional = this.productRepository.getProductByProductId(sessionCartItem.getProduct().productId());
            if (productOptional.isEmpty()) return;
            Optional<CartItem> cartItemOptional = this.cartItemRepository.findByCartItemKey_CartAndCartItemKey_Product(cart, productOptional.get());
            if (cartItemOptional.isPresent()) {
                CartItem existingCartItem = cartItemOptional.get();
                existingCartItem.setQuantity(sessionCartItem.getQuantity() + existingCartItem.getQuantity());
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setCartItemKey(new CartItemKey(cart, productOptional.get()));
                newCartItem.setQuantity(sessionCartItem.getQuantity());
                newCartItem.setPrice(sessionCartItem.getPrice().price());
                cart.getCartItems().add(newCartItem);
            }
        });
        cartRepository.save(cart);
    }

    @Override
    public void initCart(String username) {
        Cart cart = this.cartRepository.findByUserUsername(username);
        if (cart == null) {
            Optional<User> user = this.userRepository.findByUsername(username);
            if (user.isPresent()) {
                cart = new Cart();
                cart.setUser(user.get());
                this.cartRepository.save(cart);
            }
        }
    }

}

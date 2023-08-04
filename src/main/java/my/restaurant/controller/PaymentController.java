package my.restaurant.controller;

import my.restaurant.dto.PaymentDTO;
import my.restaurant.dto.PaymentModalDTO;
import my.restaurant.modal.forms.EditPaymentForm;
import my.restaurant.modal.forms.PaymentForm;
import my.restaurant.service.PaymentService;
import my.restaurant.utils.Constants;
import my.restaurant.utils.RestaurantUtils;
import my.restaurant.validation.PaymentGroup;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/user/payments")
    @PreAuthorize("hasAuthority('USER')")
    public String payments(Model model) {
        model.addAttribute(Constants.PageTitle, "List of payments");
        model.addAttribute("payments", this.paymentService.getPayments());
        return "user/payment/paymentList";
    }

    @GetMapping("/user/payments/add")
    @PreAuthorize("hasAuthority('USER')")
    public String addPayment(Model model) {
        model.addAttribute(Constants.PageTitle, "Add payment");
        model.addAttribute("paymentForm", new PaymentForm());
        return "user/payment/addPayment";
    }

    @PostMapping("/user/payments/add")
    @PreAuthorize("hasAuthority('USER')")
    public String addPayment(@Validated(PaymentGroup.class) @ModelAttribute("paymentForm") PaymentForm paymentForm, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute(Constants.PageTitle, "Add payment");

        if (bindingResult.hasErrors()) {
            return "/user/payment/addPayment";
        }

        this.paymentService.addPayment(paymentForm);
        redirectAttributes.addFlashAttribute("message", "Payment added");
        return "redirect:/user/payments";
    }

    @GetMapping("/user/payments/edit/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String editPayment(@PathVariable("id") long id, Model model) {
        model.addAttribute(Constants.PageTitle, "Edit payment");
        PaymentDTO paymentDTO = this.paymentService.getPayment(id);
        model.addAttribute("editPaymentForm", RestaurantUtils.mapPaymentDTOToEditPaymentForm(paymentDTO));
        return "user/payment/editPayment";
    }

    @PostMapping("/user/payments/edit/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String editPayment(@PathVariable("id") long id, @Validated(PaymentGroup.class) @ModelAttribute("editPaymentForm") EditPaymentForm editPaymentForm, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute(Constants.PageTitle, "Edit payment");
        if (bindingResult.hasErrors()) {
            return "/user/payment/editPayment";
        }

        this.paymentService.editPayment(editPaymentForm);

        redirectAttributes.addFlashAttribute("message", "Payment added");

        return "redirect:/user/payments";

    }

    @PostMapping("/user/payment/delete")
    @PreAuthorize("hasAuthority('USER')")
    public String deletePayment(long paymentId, RedirectAttributes redirectAttributes) {
        this.paymentService.deletePayment(paymentId);
        redirectAttributes.addFlashAttribute("message", "Payment deleted");
        return "redirect:/user/payments";
    }

    @PostMapping("user/paymentModal/add")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    public PaymentModalDTO addAddressModal(@Validated(PaymentGroup.class) @ModelAttribute("paymentForm") PaymentForm paymentForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return new PaymentModalDTO(null, bindingResult.getAllErrors());
        }
        PaymentDTO paymentDTO = this.paymentService.addPayment(paymentForm);
        return new PaymentModalDTO(paymentDTO, null);
    }
}

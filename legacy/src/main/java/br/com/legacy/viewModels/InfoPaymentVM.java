package br.com.legacy.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import androidx.databinding.ObservableField;

import com.example.library.OSEditText;
import com.google.gson.Gson;

import br.com.R;
import br.com.databinding.ActivityInfoPaymentBinding;
import br.com.goin.base.utils.Utils;
import br.com.goin.component.session.user.UserModel;
import br.com.goin.component.session.user.UserSessionInteractor;
import br.com.legacy.customViews.CustomEditText;
import br.com.legacy.managers.Iugu.IuguManager;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.managers.Iugu.IuguParameterModel;
import br.com.legacy.managers.Iugu.IuguParameterModel.DataObject;
import br.com.legacy.utils.Constants;
import br.com.legacy.viewControllers.activitites.PaymentMethodActivity;
import br.com.legacy.viewModels.base.BaseViewModel;

public class InfoPaymentVM extends BaseViewModel {

    private ObservableField<String> firstName = new ObservableField<>();
    private String lastName;
    private String cpf;
    private String cardNumber;
    private String expiracyMonth;
    private String expiracyYear;
    private String cvv;
    private String parcelsNumber;
    public String postalCode;
    public String street;
    public String streetNumber;
    public String complement;
    public String neighborhood;
    public String city;
    public String state;
    public String buyerEmail;
    public String buyerPhone;
    public PurchaseModel purchaseModel;
    public UserModel currentUser;
    public PaymentMethodActivity.PaymentMethod paymentMethod;
    private String creditCardMask;
    public String birthday;

    private UserSessionInteractor sessionInteractor = UserSessionInteractor.Companion.getInstance();

    public InfoPaymentVM(Application application) {
        super(application);
    }

    public void init(HandlerError handlerError, Intent intent, Activity activity) {
        super.init(handlerError);
        setNames(intent);
        purchaseModel = (new Gson()).fromJson(intent.getStringExtra(Constants.PURCHASE_MODEL), PurchaseModel.class);
        paymentMethod = (PaymentMethodActivity.PaymentMethod) intent.getSerializableExtra(Constants.TYPE);
        currentUser = sessionInteractor.fetchUser();
        purchaseModel.originType = (intent != null) ? intent.getStringExtra(Constants.ORIGIN_TYPE) : null;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        if (firstName != null) {
            this.firstName.set(firstName);
        }
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber.replace("-", "");
    }

    public String getExpiracyMonth() {
        return expiracyMonth;
    }

    public void setExpiracyMonth(String expiracyMonth) {
        this.expiracyMonth = expiracyMonth;
    }

    public String getExpiracyYear() {
        return expiracyYear;
    }

    public void setExpiracyYear(String expiracyYear) {
        this.expiracyYear = expiracyYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getParcelsNumber() {
        return parcelsNumber;
    }

    public void setParcelsNumber(String parcelsNumber) {
        this.parcelsNumber = parcelsNumber;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerPostalCode() {
        return postalCode;
    }

    public void setBuyerPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getBuyerStreet() {
        return street;
    }

    public void setBuyerStreet(String street) {
        this.street = street;
    }

    public String getBuyerStreetNumber() {
        return streetNumber;
    }

    public void setBuyerStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getBuyerLocationComplement() {
        return complement;
    }

    public void setBuyerLocationComplement(String complement) {
        this.complement = complement;
    }

    public String getBuyerNeighborhood() {
        return neighborhood;
    }

    public void setBuyerNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getBuyerCity() {
        return city;
    }

    public void setBuyerCity(String city) {
        this.city = city;
    }

    public String getBuyerState() {
        return state;
    }

    public void setBuyerState(String state) {
        this.state = state;
    }

    public void setCreditCardMask(String creditCardMask) {
        this.creditCardMask = creditCardMask;
    }

    public void setAsValidField(boolean validField, CustomEditText field) {
        int textSize = 12;
        if (validField) {
            field.setStyle(R.color.gray, R.color.lightGray, textSize);
        } else {
            field.setStyle(R.color.red, R.color.colorDarkRed, textSize);
        }
    }

    public boolean checkIfValidField(CustomEditText field) {
        if (field.getText() != null && !field.getText().isEmpty()) {
            setAsValidField(true, field);
            return true;
        }
        setAsValidField(false, field);
        return false;
    }

    public boolean checkIfValidEmailField(Activity activity, CustomEditText field) {
        if (field.getText() != null && !field.getText().isEmpty()) {
            OSEditText emailValid = new OSEditText.Builder(activity, field.getEditText())
                    .isEmail(true)
                    .build();
            if (emailValid.validate()) {
                setAsValidField(true, field);
                return true;
            }
        }
        setAsValidField(false, field);
        return false;
    }

    public boolean isValidCPF(CustomEditText field) {

        boolean isValid;

        if (field.getText() != null && !field.getText().isEmpty()
                && Utils.INSTANCE.isValidCPF(field.getText())) {
            setAsValidField(true, field);
            isValid = true;
        } else {

            setAsValidField(false, field);
            isValid = false;
        }

        return isValid;
    }

    public boolean verifyUserInputs(ActivityInfoPaymentBinding binding, Activity activity) {
        boolean fieldsAreValid;

        fieldsAreValid = checkIfValidField(binding.infoPaymentFirstNameField);
        fieldsAreValid = checkIfValidField(binding.infoPaymentLastNameField) && fieldsAreValid;
        fieldsAreValid = isValidCPF(binding.infoPaymentCpfField) && fieldsAreValid;

        if (fieldsAreValid) {
            setFirstName(binding.infoPaymentFirstNameField.getText());
            setLastName(binding.infoPaymentLastNameField.getText());
            setCpf(binding.infoPaymentCpfField.getText());
        }
        switch (paymentMethod) {
            case Card:
                fieldsAreValid = checkIfValidField(binding.infoPaymentCardNumber) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentExpirationMonth) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentExpirationYear) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentCvv) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentParcelsNumber) && fieldsAreValid;
                if (fieldsAreValid) {
                    setCardNumber(binding.infoPaymentCardNumber.getText());
                    setExpiracyMonth(binding.infoPaymentExpirationMonth.getText());
                    setExpiracyYear(binding.infoPaymentExpirationYear.getText());
                    setCvv(binding.infoPaymentCvv.getText());
                    setParcelsNumber(binding.infoPaymentParcelsNumber.getText());
                }
                break;
            case Boleto:
                fieldsAreValid = checkIfValidEmailField(activity, binding.infoPaymentBuyerMail) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerPhone) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerPostalCode) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerStreet) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerStreetNumber) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerNeighborhood) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerCity) && fieldsAreValid;
                fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerState) && fieldsAreValid;
                if (fieldsAreValid) {
                    setBuyerEmail(binding.infoPaymentBuyerMail.getText());
                    setBuyerPhone(binding.infoPaymentBuyerPhone.getText());
                    setBuyerPostalCode(binding.infoPaymentBuyerPostalCode.getText());
                    setBuyerStreet(binding.infoPaymentBuyerStreet.getText());
                    setBuyerStreetNumber(binding.infoPaymentBuyerStreetNumber.getText());
                    setBuyerNeighborhood(binding.infoPaymentBuyerNeighborhood.getText());
                    setBuyerCity(binding.infoPaymentBuyerCity.getText());
                    setBuyerState(binding.infoPaymentBuyerState.getText());
                }
                if (binding.infoPaymentBuyerComplement.getText() != null && !binding.infoPaymentBuyerComplement.getText().isEmpty()) {
                    setBuyerLocationComplement(binding.infoPaymentBuyerComplement.getText());
                } else {
                    setBuyerLocationComplement("");
                }
        }

        if ("INGRESSE".equals(purchaseModel.originType)){

            fieldsAreValid = checkIfValidField(binding.infoPaymentCardNumber) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentExpirationMonth) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentExpirationYear) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentCvv) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentParcelsNumber) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerPostalCode) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerStreet) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerStreetNumber) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerNeighborhood) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerCity) && fieldsAreValid;
            fieldsAreValid = checkIfValidField(binding.infoPaymentBuyerState) && fieldsAreValid;
            if (fieldsAreValid) {
                setCardNumber(binding.infoPaymentCardNumber.getText());
                setExpiracyMonth(binding.infoPaymentExpirationMonth.getText());
                setExpiracyYear(binding.infoPaymentExpirationYear.getText());
                setCvv(binding.infoPaymentCvv.getText());
                setParcelsNumber(binding.infoPaymentParcelsNumber.getText());
                setBuyerPostalCode(binding.infoPaymentBuyerPostalCode.getText());
                setBuyerStreet(binding.infoPaymentBuyerStreet.getText());
                setBuyerStreetNumber(binding.infoPaymentBuyerStreetNumber.getText());
                setBuyerNeighborhood(binding.infoPaymentBuyerNeighborhood.getText());
                setBuyerCity(binding.infoPaymentBuyerCity.getText());
                setBuyerState(binding.infoPaymentBuyerState.getText());
                setBirthday(binding.infoPaymentBirthdayField.getText());
            }
        }

        return fieldsAreValid;
    }

    void setNames(Intent intent) {
        String name = intent.getStringExtra(Constants.USER_NAME);
        if (name != null) {
            String[] names = name.split(" ");
            setFirstName(names[0]);
            if (names.length > 1) {
                setLastName(names[names.length - 1]);
            }
        }
    }

    public boolean showAddress() {

        boolean showAddres = false;

        if (paymentMethod != null && paymentMethod == PaymentMethodActivity.PaymentMethod.Boleto) {
            showAddres = true;
        }
        if ("INGRESSE".equals(purchaseModel.originType)) {

            showAddres = true;
        }
        return showAddres;
    }

   public boolean isTicket() {

        boolean isTicket = false;

        if (paymentMethod != null && paymentMethod == PaymentMethodActivity.PaymentMethod.Boleto) {
            isTicket = true;
        }

        return isTicket;
    }

    public void getIuguPaymentToken(Activity activity, IuguManager.IuguServiceHandler handler) {
        IuguParameterModel bodyParams = new IuguParameterModel();
        DataObject data = new DataObject();
        data.number = this.cardNumber;
        data.first_name = this.firstName.get();
        data.last_name = this.lastName;
        data.month = this.expiracyMonth;
        data.year = this.expiracyYear;
        data.verification_value = this.cvv;
        bodyParams.data = data;
        bodyParams.account_id = Constants.IUGU_ACCOUNT_ID;
        bodyParams.method = "credit_card";
        bodyParams.test = true;  // TODO !!! set to FALSE in PROUCTION !!!


        IuguManager iuguManager = new IuguManager(activity, handler);
        iuguManager.requestIuguPaymentToken(bodyParams);
    }

    public void updatePurchaseModel() {
        switch (paymentMethod) {
            case Card:
                purchaseModel.paymentType = PurchaseModel.PaymentType.CreditCard;
                purchaseModel.paymentInfo = createCardPaymentInfo();
                break;
            case Boleto:
                purchaseModel.paymentType = PurchaseModel.PaymentType.BankSlip;
                purchaseModel.paymentInfo = createBankSlipPaymentInfo();
                break;
            case PayPal:
                break;
        }
    }

    PurchaseModel.PaymentInfo createCardPaymentInfo() {
        PurchaseModel.PaymentInfo paymentInfo;
        if (purchaseModel.paymentInfo == null) {
            paymentInfo = new PurchaseModel().new PaymentInfo();
        } else {
            paymentInfo = purchaseModel.paymentInfo;
        }
        paymentInfo.CPF = getCpf();
        paymentInfo.installments = Integer.parseInt(getParcelsNumber());

        paymentInfo.displayNumber = creditCardMask.equals("") ? getCardNumber() : creditCardMask;
        PurchaseModel.PaymentData data = new PurchaseModel().new PaymentData();
        paymentInfo.birthday = (getBirthday() != null) ? getBirthday() : "";
        data.firstName = getFirstName();
        data.lastName = getLastName();
        data.number = getCardNumber();
        data.verificationValue = getCvv();
        data.expiracyMonth = getExpiracyMonth();
        data.expiracyYear = ("INGRESSE".equals(purchaseModel.originType))
                ? getExpiracyYear() : "20" + getExpiracyYear();
        data.cvv = getCvv();

        PurchaseModel.AddressInfo addressInfo = new PurchaseModel().new AddressInfo();

        if("INGRESSE".equals(purchaseModel.originType)) {

            addressInfo.city = getBuyerCity();
            addressInfo.neighborhood = getBuyerNeighborhood();
            addressInfo.postalCode = getBuyerPostalCode();
            addressInfo.state = getBuyerState();
            addressInfo.street = getBuyerStreet();
            addressInfo.streetNumber = getBuyerStreetNumber();
            addressInfo.complement = getBuyerLocationComplement();
        } else {

            addressInfo.city = "";
            addressInfo.neighborhood = "";
            addressInfo.postalCode = "";
            addressInfo.state = "";
            addressInfo.street = "";
            addressInfo.streetNumber = "";
            addressInfo.complement = "";
        }

        paymentInfo.data = data;
        paymentInfo.addressInfo = addressInfo;
        return paymentInfo;
    }

    PurchaseModel.PaymentInfo createBankSlipPaymentInfo() {
        PurchaseModel.PaymentInfo paymentInfo = new PurchaseModel().new PaymentInfo();
        paymentInfo.CPF = getCpf();
        PurchaseModel.PaymentData data = new PurchaseModel().new PaymentData();
        data.firstName = getFirstName();
        data.lastName = getLastName();

        PurchaseModel.AddressInfo addressInfo = new PurchaseModel().new AddressInfo();
        addressInfo.postalCode = getBuyerPostalCode();
        addressInfo.street = getBuyerStreet();
        addressInfo.streetNumber = getBuyerStreetNumber();
        addressInfo.complement = getBuyerLocationComplement();
        addressInfo.neighborhood = getBuyerNeighborhood();
        addressInfo.city = getBuyerCity();
        addressInfo.state = getBuyerState();

        data.buyerEmail = getBuyerEmail();
        data.buyerPhone = getBuyerPhone();

        paymentInfo.addressInfo = addressInfo;
        paymentInfo.data = data;
        return paymentInfo;
    }
}

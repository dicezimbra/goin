package br.com.legacy.managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.dtos.BuyTicketCreditCardInput;
import br.com.legacy.managers.dtos.BuyTicketBankSlipInput;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.managers.dtos.TicketDetails;
import br.com.legacy.managers.dtos.TransferTicketInput;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.models.TicketModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by appsimples on 19/12/17.
 */

public class TicketsManager extends BaseManager {

    ProgressDialog progressDialog;
    TicketsHandler ticketsHandler;
    public MyTicketsHandler myTicketsHandler;
    public PurchaseDetailsHandler purchaseDetailsHandler;
    public BuyTicketHandler buyTicketHandler;


    public interface TicketsHandler {
        void onReceiveTickets(List<TicketModel> tickets);

        void onTicketBought(String ticketIdWithDate, String eventId);

        void onError(String error);
    }

    public interface MyTicketsHandler {
        void onReceiveIndividualTicketsList(List<TicketModel> tickets);

        void onReceiveVipBoxTicketsList(List<TicketModel> tickets);

        void onIndividualTicketsError(String error);

        void onVipBoxTicketsError(String error);
    }

    public interface PurchaseDetailsHandler {
        void onReceiveTicket(PurchaseModel purchaseModel);

        void onError(String error);
    }

    public interface BuyTicketHandler {
        void onSuccessToBuyTicket(PurchaseModel purchaseModel);

        void onFailureToBuyTicket(String error);

        void onSuccessToTransferTicket();

        void onFailureToTransferTicket(String error);
    }

    public TicketsManager(Activity activity) {
        super(activity);
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        progressDialog.hide();
    }

    public TicketsManager(Activity activity, TicketsHandler handler) {
        super(activity);
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        ticketsHandler = handler;
        progressDialog.hide();
    }

    public void buyTicket(final TicketModel ticket, final Integer quantity) {

        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                if (ticketsHandler != null) {
                    ticketsHandler.onTicketBought(response.buyTicket, ticket.event.id);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (ticketsHandler != null) {
                    ticketsHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                if (ticket.id != null && ticket.clubId != null && ticket.event.id != null && quantity != null)
                    serviceManager.buyTicket(TicketsManager.this, handler,
                            ticket.id, ticket.clubId, ticket.event.id, quantity);
            }
        });
    }

    public void confirmTicket(final String ticketId, final String paypalData) {

        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                Log.d("paymentExample", response.confirmBuyTicket);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (ticketsHandler != null) {
                    ticketsHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                if (ticketId != null)
                    serviceManager.confirmTicket(TicketsManager.this, handler, ticketId, paypalData);
            }
        });
    }

    public void cancelTicket(final String ticketId, final String eventId) {

        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                Log.d("paymentExample", response.cancelBuyTicket);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (ticketsHandler != null) {
                    ticketsHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                if (ticketId != null)
                    serviceManager.cancelTicket(TicketsManager.this, handler, ticketId, eventId);
            }
        });
    }

    private static List<TicketModel> mapListDtoToModel(List<TicketDetails> dtos, TicketModel.CardType cardType) {
        List<TicketModel> list = new ArrayList<>();
        for (TicketDetails t : dtos) {
            TicketModel ticketModel = new TicketModel();
            ticketModel.id = t.id;
            ticketModel.price = t.price;
            ticketModel.name = t.name;
            ticketModel.description = t.description;
            ticketModel.total = t.quantity;
            ticketModel.qrCode = t.qrCode;
            ticketModel.eventAddress = t.eventAddress;

           ticketModel.discount = t.discount;
           ticketModel.discountPercent = t.discountPercent;
           ticketModel.discountValue = t.discountValue;
           ticketModel.finalValue = t.finalValue;
           ticketModel.originUrl = t.originUrl;
            Calendar eventDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            Calendar purchaseDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            Calendar startEventDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            Calendar endEventDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

            eventDateCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            purchaseDateCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            startEventDateCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            endEventDateCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));


            if (t.eventDate != null) {

                eventDateCalendar.setTimeInMillis(t.eventDate);
                ticketModel.date = eventDateCalendar;
            }
            if (t.paymentDate != null) {
                purchaseDateCalendar.setTimeInMillis(t.paymentDate);
                ticketModel.purchaseDate = purchaseDateCalendar;
            }
//            if (t.startEventDate != null && t.endEventDate != null){
//                startEventDateCalendar.setTimeInMillis(t.startEventDate);
//                endEventDateCalendar.setTimeInMillis(t.endEventDate);
//                ticketModel.startEventDate =  startEventDateCalendar;
//                ticketModel.endEventDate = endEventDateCalendar;
//            }

            ticketModel.event = t.event;
            ticketModel.isHalfPrice = t.isHalfPrice;
            ticketModel.remaining = t.remaining;
            ticketModel.club = t.clubName;
            ticketModel.eventName = t.eventName;
            ticketModel.bought = t.numTickets == null ? 0 : t.numTickets;
            ticketModel.userName = t.buyerName;
            ticketModel.isVipBox = t.enumTicketType != null && t.enumTicketType.equals(PurchaseModel.TicketType.VIP);
            ticketModel.vipCapacity = t.capacity == null ? 0 : t.capacity;
            if (ticketModel.isVipBox && cardType.equals(TicketModel.CardType.MY_TICKET)) {
                ticketModel.ticketCardType = TicketModel.CardType.MY_TICKET_VIP_BOX;
            } else {
                ticketModel.ticketCardType = cardType;
            }
            if (t.paymentStatus != null) {
                if (ticketModel.isVipBox) {
                    switch (t.paymentStatus) {
                        case Unpaid:
                            ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_PENDING;
                            break;
                        case Pending:
                            ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_PENDING;
                            break;
                        case Confirmed:
                            ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_CONFIRMED;
                            break;
                        case AwaitingFriends:
                            ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_FRIENDS_PENDING;
                            break;
                        default:
                            ticketModel.buyStatus = TicketModel.TicketStatus.PENDING;

                    }
                } else {
                    switch (t.paymentStatus) {
                        case Confirmed:
                            ticketModel.buyStatus = TicketModel.TicketStatus.CONFIRMED;
                            break;
                        case Pending:
                            ticketModel.buyStatus = TicketModel.TicketStatus.PENDING;
                            break;
                        default:
                            ticketModel.buyStatus = TicketModel.TicketStatus.PENDING;
                    }
                }

            } else {
                if (cardType != null && cardType.equals(TicketModel.CardType.TICKET_FOR_SELL)) {
                    ticketModel.buyStatus = TicketModel.TicketStatus.FOR_SELL;
                } else {
                    ticketModel.buyStatus = TicketModel.TicketStatus.PENDING;
                }
            }
            list.add(ticketModel);
        }
        return list;
    }

    public void requestMyIndividualTickets(final String lastTicketId, final Integer qtdTickets) {
        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<TicketDetails>>>() {
            @Override
            public void onResponse(HashMap<String, List<TicketDetails>> response) {

                List<TicketModel> tickets = new ArrayList<TicketModel>();

                if (myTicketsHandler != null) {
                    List<TicketDetails> list = response.get("getMyTickets");
                    tickets = mapListDtoToModel(list, TicketModel.CardType.MY_TICKET);

                }
                myTicketsHandler.onReceiveIndividualTicketsList(tickets);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (myTicketsHandler != null) {
                    myTicketsHandler.onIndividualTicketsError(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getMyIndividualTickets(TicketsManager.this, handler, lastTicketId, qtdTickets);
            }
        });
    }

    public void requestMyVipBoxTickets(final String lastTicketId, final Integer qtdTickets) {
        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<TicketDetails>>>() {
            @Override
            public void onResponse(HashMap<String, List<TicketDetails>> response) {
                if (myTicketsHandler != null) {
                    List<TicketDetails> list = response.get("getMyVipTickets");
                    myTicketsHandler.onReceiveVipBoxTicketsList(mapListDtoToModel(list, TicketModel.CardType.MY_TICKET));
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (myTicketsHandler != null) {
                    myTicketsHandler.onVipBoxTicketsError(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getMyVipBoxTickets(TicketsManager.this, handler, lastTicketId, qtdTickets);
            }
        });
    }

    public void requestEventTickets(final String eventId, String coupon) {
        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<TicketDetails>>>() {
            @Override
            public void onResponse(HashMap<String, List<TicketDetails>> response) {
                if (ticketsHandler != null && response != null) {
                    List<TicketDetails> list = response.get("getEventTickets");
                    ticketsHandler.onReceiveTickets(mapListDtoToModel(list, TicketModel.CardType.TICKET_FOR_SELL));
                }

                onlineCall(eventId, coupon);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                onlineCall(eventId, coupon);
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                cacheServiceManager.getEventTickets(handler, eventId, sharedPreferencesControl);
            }
        });
    }

    private void onlineCall(String eventId, String coupon) {
        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<TicketDetails>>>() {
            @Override
            public void onResponse(HashMap<String, List<TicketDetails>> response) {
                if (ticketsHandler != null && response != null) {
                    List<TicketDetails> list = response.get("getEventTickets");
                    ticketsHandler.onReceiveTickets(mapListDtoToModel(list, TicketModel.CardType.TICKET_FOR_SELL));
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (ticketsHandler != null) {
                    ticketsHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getEventTickets(TicketsManager.this, handler, eventId, coupon);
            }
        });
    }

    public void requestTicketDetail(final String ticketId) {

        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                if (purchaseDetailsHandler != null) {
                    TicketDetails ticketDetails = response.getMyTicketDetail;
                    purchaseDetailsHandler.onReceiveTicket(mapTicketDetailsToPurchaseModel(ticketDetails));
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (purchaseDetailsHandler != null) {
                    purchaseDetailsHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getMyTicketDetails(TicketsManager.this, handler, ticketId);
            }
        });
    }

    public void updatePartnerInfo(String ingresseToken, String originType, String userId) {
        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.updatePartnerInfo(ingresseToken, originType, userId, handler, TicketsManager.this);
            }
        });
    }

    public void buyTicketCreditCard(final PurchaseModel purchaseModel, final String token, final String displayNumber) {
        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                if (buyTicketHandler != null) {
                    TicketDetails ticketDetails = response.buyTicketCreditCard;
                    buyTicketHandler.onSuccessToBuyTicket(mapTicketDetailsToPurchaseModel(ticketDetails));
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (buyTicketHandler != null) {
                    buyTicketHandler.onFailureToBuyTicket(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.buyTicketCreditCard(TicketsManager.this, handler, mapModelCreditCardToInputDTO(purchaseModel, token, displayNumber));
            }
        });
    }

    public void buyTicketBankSlip(final PurchaseModel purchaseModel) {
        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                if (buyTicketHandler != null) {
                    TicketDetails ticketDetails = response.buyTicketBankSlip;
                    buyTicketHandler.onSuccessToBuyTicket(mapTicketDetailsToPurchaseModel(ticketDetails));
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (buyTicketHandler != null) {
                    buyTicketHandler.onFailureToBuyTicket(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.buyTicketBankSlip(TicketsManager.this, handler, mapModelBankSlipToInputDTO(purchaseModel));
            }
        });
    }

    public void transferTicket(final String ticketId, final PurchaseModel.TicketsInfo ticketsInfo) {
        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                if (buyTicketHandler != null) {
                    buyTicketHandler.onSuccessToTransferTicket();
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (buyTicketHandler != null) {
                    buyTicketHandler.onFailureToTransferTicket(error.message);
                }
            }
        };

        final TransferTicketInput transferTicketInput = new TransferTicketInput();
        transferTicketInput.myTicketId = ticketId;
        transferTicketInput.transferToInput = ticketsInfo;
        // clear this variable (backend not expecting)
        transferTicketInput.transferToInput.halfPriceInfo = null;

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.transferTicket(TicketsManager.this, handler, transferTicketInput);
            }
        });
    }

    PurchaseModel.PaymentType getPaymentType(String type) {
        switch (type) {
            case "CreditCard":
                return PurchaseModel.PaymentType.CreditCard;
            case "BankSlip":
                return PurchaseModel.PaymentType.BankSlip;
            case "PayPal":
                return PurchaseModel.PaymentType.PayPal;
        }
        return null;
    }

    PurchaseModel mapTicketDetailsToPurchaseModel(TicketDetails ticketDetails) {
        PurchaseModel purchaseModel = new PurchaseModel();
        // infos dos tickets
        purchaseModel.eventName = ticketDetails.eventName;
        purchaseModel.eventDate = ticketDetails.eventDate;
        purchaseModel.eventAddress = ticketDetails.eventAddress;
        purchaseModel.ticketName = ticketDetails.name;
        purchaseModel.price = ticketDetails.price;
        purchaseModel.buyerName = ticketDetails.buyerName;
        purchaseModel.ticketId = ticketDetails.ticketId;
        purchaseModel.purchaseDate = ticketDetails.paymentDate;
        purchaseModel.eventId = ticketDetails.eventId;
        purchaseModel.vipCapacity = ticketDetails.capacity;

        if (ticketDetails.ticketsInfo != null) {
            purchaseModel.ticketsInfo = ticketDetails.ticketsInfo;
            for (int i = 0; i < purchaseModel.ticketsInfo.size(); i++) {
                purchaseModel.ticketsInfo.get(i).halfPriceInfo = ticketDetails.ticketsInfo.get(i).halfPriceId;
            }
            purchaseModel.totalValue = purchaseModel.ticketsInfo.size() * purchaseModel.price;
        } else {
            purchaseModel.ticketsInfo = new ArrayList<>();
        }
        // infos da compra
        if (ticketDetails.paymentInfo != null) {
            purchaseModel.isMyTicket = true;
            purchaseModel.paymentInfo = new PurchaseModel().new PaymentInfo();

            PurchaseModel.PaymentData data = new PurchaseModel().new PaymentData();
            // Credit Card
            data.firstName = ticketDetails.paymentInfo.buyerName;
            // Bank Slip
            purchaseModel.paymentInfo.addressInfo = new PurchaseModel().new AddressInfo();
            purchaseModel.paymentInfo.addressInfo.postalCode = ticketDetails.paymentInfo.postalCode;
            purchaseModel.paymentInfo.addressInfo.street = ticketDetails.paymentInfo.street;
            purchaseModel.paymentInfo.addressInfo.streetNumber = ticketDetails.paymentInfo.streetNumber;
            purchaseModel.paymentInfo.addressInfo.complement = ticketDetails.paymentInfo.complement;
            purchaseModel.paymentInfo.addressInfo.neighborhood = ticketDetails.paymentInfo.neighborhood;
            purchaseModel.paymentInfo.addressInfo.city = ticketDetails.paymentInfo.city;
            purchaseModel.paymentInfo.addressInfo.state = ticketDetails.paymentInfo.state;
            data.buyerEmail = ticketDetails.paymentInfo.buyerEmail;
            data.buyerPhone = ticketDetails.paymentInfo.buyerPhone;
            data.bankSlipPDF = ticketDetails.paymentInfo.pdf;

            purchaseModel.paymentInfo.data = data;
            purchaseModel.paymentInfo.CPF = ticketDetails.paymentInfo.CPF;
            purchaseModel.paymentInfo.installments = ticketDetails.paymentInfo.months;
            purchaseModel.paymentInfo.displayNumber = ticketDetails.paymentInfo.displayNumber;
            purchaseModel.paymentType = getPaymentType(ticketDetails.paymentInfo.type);
        } else {
            purchaseModel.isMyTicket = false;
            purchaseModel.paymentInfo = new PurchaseModel().new PaymentInfo();
            purchaseModel.paymentInfo.CPF = "";
            purchaseModel.paymentInfo.installments = 0;
            purchaseModel.paymentInfo.displayNumber = "";
        }
        purchaseModel.isVipBox = ticketDetails.enumTicketType != null && ticketDetails.enumTicketType.equals(PurchaseModel.TicketType.VIP);
        if (ticketDetails.paymentStatus != null) {
            if (purchaseModel.isVipBox) {
                switch (ticketDetails.paymentStatus) {
                    case Unpaid:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Pending;
                        break;
                    case Pending:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Pending;
                        break;
                    case Confirmed:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Confirmed;
                        break;
                    case AwaitingFriends:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.VipBoxFriendsPending;
                        break;
                    default:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Pending;

                }
            } else {
                switch (ticketDetails.paymentStatus) {
                    case Confirmed:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Confirmed;
                        break;
                    case Pending:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Pending;
                        break;
                    default:
                        purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Pending;
                }
            }

        } else {
            purchaseModel.paymentStatus = PurchaseModel.PaymentStatus.Pending;
        }
        purchaseModel.vipBoxId = ticketDetails.vipBoxId;
        return purchaseModel;
    }

    BuyTicketCreditCardInput mapModelCreditCardToInputDTO(PurchaseModel purchaseModel, String token, String displayNumber) {
        BuyTicketCreditCardInput input = new BuyTicketCreditCardInput();
        input.ticketId = purchaseModel.ticketId;
        input.isSplittedBox = purchaseModel.isSplittedVipBox;
        if (purchaseModel.isVipBox != null && purchaseModel.isVipBox) {
            input.ticketType = PurchaseModel.TicketType.VIP;
        } else {
            input.ticketType = PurchaseModel.TicketType.REGULAR;
        }
        List<BuyTicketCreditCardInput.TicketInfoInput> list = new ArrayList<>();
        for (PurchaseModel.TicketsInfo ticketsInfo : purchaseModel.ticketsInfo) {
            BuyTicketCreditCardInput.TicketInfoInput info = new BuyTicketCreditCardInput().new TicketInfoInput();
            info.userId = ticketsInfo.userId;
            info.name = ticketsInfo.name;
            info.RG = ticketsInfo.RG;
            info.halfPriceId = ticketsInfo.halfPriceInfo;
            list.add(info);
        }
        input.ticketsInfo = list;
        input.vipBoxId = purchaseModel.vipBoxId;
        BuyTicketCreditCardInput.CreditCardInput creditCardInput = new BuyTicketCreditCardInput().new CreditCardInput();
        creditCardInput.token = token;
        creditCardInput.cardHolder = purchaseModel.paymentInfo.data.firstName + " " + purchaseModel.paymentInfo.data.lastName;
        creditCardInput.CPF = purchaseModel.paymentInfo.CPF;
        creditCardInput.displayNumber = displayNumber;

        String expiracyMonth;
        if (purchaseModel.paymentInfo.data.expiracyMonth.length() == 1) {

            expiracyMonth = "0" + purchaseModel.paymentInfo.data.expiracyMonth;
        } else {

            creditCardInput.expiracyMonth = purchaseModel.paymentInfo.data.expiracyMonth;
        }

        input.creditCardInfo = creditCardInput;

        input.eventId = purchaseModel.eventId;
        input.originType = purchaseModel.originType;
        input.creditCardInfo.expiracyYear = purchaseModel.paymentInfo.data.expiracyYear;
        input.creditCardInfo.cvv = purchaseModel.paymentInfo.data.cvv;
        input.addressInfo = purchaseModel.paymentInfo.addressInfo;
        input.creditCardInfo.birthday = purchaseModel.paymentInfo.birthday;

        return input;
    }

    BuyTicketBankSlipInput mapModelBankSlipToInputDTO(PurchaseModel purchaseModel) {
        BuyTicketBankSlipInput input = new BuyTicketBankSlipInput();

        input.ticketId = purchaseModel.ticketId;
        input.isSplittedBox = purchaseModel.isSplittedVipBox;
        if (purchaseModel.isVipBox != null && purchaseModel.isVipBox) {
            input.ticketType = PurchaseModel.TicketType.VIP;
        } else {
            input.ticketType = PurchaseModel.TicketType.REGULAR;
        }
        List<BuyTicketBankSlipInput.TicketInfoInput> list = new ArrayList<>();
        for (PurchaseModel.TicketsInfo ticketsInfo : purchaseModel.ticketsInfo) {
            BuyTicketBankSlipInput.TicketInfoInput info = new BuyTicketBankSlipInput().new TicketInfoInput();
            info.userId = ticketsInfo.userId;
            info.name = ticketsInfo.name;
            info.RG = ticketsInfo.RG;
            info.halfPriceId = ticketsInfo.halfPriceInfo;
            list.add(info);
        }
        input.ticketsInfo = list;
        input.vipBoxId = purchaseModel.vipBoxId;

        BuyTicketBankSlipInput.BankSlipInput bankSlipInput = new BuyTicketBankSlipInput().new BankSlipInput();
        bankSlipInput.CPF = purchaseModel.paymentInfo.CPF;
        bankSlipInput.buyerEmail = purchaseModel.paymentInfo.data.buyerEmail;
        bankSlipInput.buyerPhone = purchaseModel.paymentInfo.data.buyerPhone;
        bankSlipInput.buyerName = purchaseModel.paymentInfo.data.firstName + " " + purchaseModel.paymentInfo.data.lastName;

        PurchaseModel.AddressInfo addressInfo = new PurchaseModel().new AddressInfo();
        addressInfo.postalCode = purchaseModel.paymentInfo.addressInfo.postalCode;
        addressInfo.street = purchaseModel.paymentInfo.addressInfo.street;
        addressInfo.streetNumber = purchaseModel.paymentInfo.addressInfo.streetNumber;
        addressInfo.complement = purchaseModel.paymentInfo.addressInfo.complement;
        addressInfo.neighborhood = purchaseModel.paymentInfo.addressInfo.neighborhood;
        addressInfo.city = purchaseModel.paymentInfo.addressInfo.city;
        addressInfo.state = purchaseModel.paymentInfo.addressInfo.state;

        input.addressInfo = addressInfo;
        input.bankSlipInfo = bankSlipInput;
        return input;
    }
}

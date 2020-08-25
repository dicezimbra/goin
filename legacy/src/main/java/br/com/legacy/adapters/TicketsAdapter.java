package br.com.legacy.adapters;

import android.app.Activity;
import android.graphics.Paint;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.legacy.models.TicketModel;
import br.com.R;
import br.com.legacy.utils.TypefaceUtil;
import br.com.databinding.ItemCardTicketBinding;

import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder> {

    TicketModel.TicketItemHandler handler;
    List<TicketModel> tickets;
    Activity activity;

    public TicketsAdapter(Activity activity, List<TicketModel> list, TicketModel.TicketItemHandler handler) {
        this.tickets = list;
        this.handler = handler;
        this.activity = activity;

        TypefaceUtil.initilize(activity);
    }

    @Override
    public TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCardTicketBinding binding = ItemCardTicketBinding.inflate(inflater, parent, false);
        return new TicketsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final TicketsViewHolder holder, final int position) {
        final TicketModel ticketModel = tickets.get(position);
        ticketModel.activity = activity;
        ticketModel.handler = handler;

        holder.bind(ticketModel);
    }

    @Override
    public int getItemCount() {
        return this.tickets.size();
    }

    public static class TicketsViewHolder extends RecyclerView.ViewHolder {

        private final ItemCardTicketBinding binding;
        public CardView itemContainer;

        public TicketsViewHolder(ItemCardTicketBinding binding) {
            super(binding.getRoot());
            this.itemContainer = binding.getRoot().findViewById(R.id.card_view_ticket);
            this.binding = binding;
        }

        public ItemCardTicketBinding bind(final TicketModel item) {

            TypefaceUtil.boldFont(binding.ticketName, binding.ticketPrice, binding.ticketEventName, binding.ticketDate);
            TypefaceUtil.regularFont(binding.ticketDescription, binding.ticketPurchaseDate,
                    binding.qtdPurchasedTickets, binding.ticketEventClub, binding.ticketButton);

            binding.setTicket(item);
            binding.ticketName.setAllCaps(true);
            binding.ticketEventName.setAllCaps(true);

            if (item.handler != null) {
                binding.ticketButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.onClickTicketButton();
                    }
                });

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.onClickTicketContainer();
                    }
                });
            }

            if(item.hasTicketDiscount() != null && item.hasTicketDiscount()){
                binding.ticketPrice.setPaintFlags(binding.ticketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            binding.executePendingBindings();

            return binding;
        }

    }
}

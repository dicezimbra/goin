package br.com.legacy.models;

import java.io.Serializable;
import java.util.List;

public class SearchFilterModel implements Serializable {
    public int currentPage;
    public String totalPages;
    public String totalItems;
    public List<br.com.goin.component.model.event.Event> events;
    public List<ClubModel> clubModels;

    public SearchFilterModel(int currentPage, String totalPages, String totalItems, List<br.com.goin.component.model.event.Event> events, List<ClubModel> clubModels) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.events = events;
        this.clubModels = clubModels;
    }

    public boolean hasMorePages(){
        try{
            int totalPages = Integer.parseInt(this.totalPages);
            int currentPage = this.currentPage;

            return currentPage < totalPages;
        }catch (Exception e){}

        return false;
    }
}

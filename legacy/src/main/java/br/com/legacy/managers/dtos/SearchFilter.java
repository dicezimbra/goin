package br.com.legacy.managers.dtos;

import java.io.Serializable;
import java.util.List;

import br.com.goin.component.model.event.api.response.ApiEvent;

public class SearchFilter implements Serializable {
    public int currentPage;
    public String totalPages;
    public String totalItems;
    public List<ApiEvent> events;
    public List<Club> clubs;
}

package br.com.legacy.managers;

import android.app.Activity;

import java.util.List;

import br.com.legacy.managers.dtos.CitiesList;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.StatesList;
import br.com.legacy.models.CitiesModel;
import br.com.legacy.models.StatesModel;

public class LocaleManager extends BaseManager {

    public LocaleManagerCitiesRequestHandler citiesRequestHandler;
    public LocaleManagerStatesRequestHandler statesRequestHandler;

    public interface LocaleManagerCitiesRequestHandler {
        void onReceiveCities(List<CitiesModel> cities, String oldCity);

        void onFailureToReceiveCities(String message);
    }

    public interface LocaleManagerStatesRequestHandler {
        void onReceiveStates(List<StatesModel> states, String oldState, String oldCity);

        void onFailureToReceiveStates(String message);
    }

    public LocaleManager(Activity activity) {
        super(activity);
        if (activity instanceof LocaleManagerCitiesRequestHandler)
            this.citiesRequestHandler = (LocaleManagerCitiesRequestHandler) activity;

        if (activity instanceof LocaleManagerStatesRequestHandler)
            this.statesRequestHandler = (LocaleManagerStatesRequestHandler) activity;
    }


    public void requestStatesList(String oldState, String oldCity) {

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {

                serviceManager.getStates(LocaleManager.this, new RequestResponseHandler<StatesList>() {
                    @Override
                    public void onResponse(StatesList response) {
                        if (response != null)
                            statesRequestHandler.onReceiveStates(response.searchStates, oldState, oldCity);
                    }

                    @Override
                    public void onFailure(ErrorResponse error) {
                        statesRequestHandler.onFailureToReceiveStates(error.message);
                    }
                });
            }
        });
    }

    public void requestCitiesList(final String state, String oldCity) {

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getCitiesByState(state, LocaleManager.this, new RequestResponseHandler<CitiesList>() {

                    @Override
                    public void onResponse(CitiesList response) {
                        if (response != null)
                            citiesRequestHandler.onReceiveCities(response.searchCities, oldCity);
                    }

                    @Override
                    public void onFailure(ErrorResponse error) {
                        citiesRequestHandler.onFailureToReceiveCities(error.message);

                    }
                });
            }
        });
    }

}

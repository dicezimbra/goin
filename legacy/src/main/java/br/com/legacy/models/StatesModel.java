package br.com.legacy.models;

public class StatesModel {

    public String state;

    public StatesModel(String state) {
        this.state = state;

    }

    @Override
    public String toString() {
        return state;
    }
}

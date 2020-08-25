package br.com.legacy.models;

import java.io.Serializable;
import androidx.databinding.BaseObservable;

public class EventInformation extends BaseObservable implements Serializable {

    public String duration;
    public String releaseDate;
    public String coverImage;
    public String parentalRating;
    public String trailer;
    public String production;
    public String musicComposition;
}

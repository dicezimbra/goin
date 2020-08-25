package br.com.legacy.newlayout.searchfilter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import br.com.BR;
import br.com.databinding.ActivityNewFilterSearchBinding;
import br.com.goin.base.utils.Utils;
import br.com.goin.component.model.category.CategoriesInteractorImpl;
import br.com.goin.component.model.category.Category;
import br.com.goin.component.model.category.SubcategoriesModel;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.components.SelectableCircleComponentFIlter;
import br.com.legacy.managers.LocaleManager;
import br.com.legacy.managers.SubcategoriesManager;
import br.com.legacy.models.CitiesModel;
import br.com.legacy.models.StatesModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.ToolbarConfigurator;
import br.com.legacy.utils.TypefaceUtil;
import br.com.legacy.viewControllers.activitites.base.BaseActivity;
import br.com.legacy.viewModels.FilterSearchVM;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewFilterSearchActivity extends BaseActivity implements
        SubcategoriesManager.SubcategoriesRequestHandler, SelectableCircleComponentFIlter.SelectableCircleHandler {

    ActivityNewFilterSearchBinding binding;
    public FilterSearchVM filterSearchVM;
    public SubcategoriesManager subcategoriesManager;
    public LocaleManager localeManager;
    public Category.CategoryType categoryType;
    public String selectedCategorie;

    ProgressDialog progressDialog;
    public ObservableBoolean noSubcategoriesMessage = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    List<SubcategoriesModel> subCategoriesList = new ArrayList<>();

    public ObservableList<SelectableCircleComponentFIlter> categoriesList = new ObservableArrayList<>();
    ArrayList<String> filtersList;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    List<StatesModel> statesModelList = new ArrayList<>();
    List<CitiesModel> citiesModelList = new ArrayList<>();


    @BindView(br.com.R2.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;

    @BindView(br.com.R2.id.toolbar)
    View toolbar;

    @BindView(br.com.R2.id.buttonClear)
    Button buttonClear;

   // @BindView(R.id.search_search_button)
  //  Button buttonFilter;

    @BindView(br.com.R2.id.txtDateFrom)
    TextView txtDateFrom;

    @BindView(br.com.R2.id.txtDateTo)
    TextView txtDateTo;

    @BindView(br.com.R2.id.dateLabelFrom)
    TextView dateLabelFrom;

    @BindView(br.com.R2.id.dateLabelTo)
    TextView dateLabelTo;

    @BindView(br.com.R2.id.textDateLabel)
    TextView textDateLabel;

    @BindView(br.com.R2.id.textViewCategoryName)
    TextView textViewCategoryName;

    public Long calendarStartDateLong, calendarEndDateLong;
    public String finalPrice, cityFilter, stateFilter;
    private KeyListener listener;
    Disposable disposable;

    public Integer stateSelected;
    private FilterDTO filterDTO;

    @SuppressLint({"ResourceType", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityNewFilterSearchBinding) this.setLayoutId(R.layout.activity_new_filter_search, BR.activity);
        filterSearchVM = (FilterSearchVM) linkViewModel(FilterSearchVM.class);

        ButterKnife.bind(this);

        Coordinator.setStatusBarColor(this, getString(R.color.grayBackgroundBoard), toolbar);
        ToolbarConfigurator.INSTANCE.configToolbar(toolbar, getString(R.string.filter_search), R.drawable.ic_arrow_back_orange, 0);

        setupManagers();

        Intent intent = getIntent();
        this.categoryType = (Category.CategoryType) intent.getSerializableExtra(Constants.TYPE);
        this.selectedCategorie = intent.getStringExtra(Constants.CATEGORY_ID);
        this.textViewCategoryName.setText(intent.getStringExtra("CATEGORY_NAME"));


        Serializable serializable = getIntent().getExtras().getSerializable("filterDTO");
        if (serializable != null) {
            filterDTO = (FilterDTO) serializable;
        }

        if (Category.Type.place.equals(categoryType)) {
            binding.textViewlabelPrice.setVisibility(View.GONE);
            binding.searchPriceRangebar.setVisibility(View.GONE);
            binding.textViewValue.setVisibility(View.GONE);
            binding.textDateLabel.setVisibility(View.GONE);
            binding.dateLabelFrom.setVisibility(View.GONE);
            binding.txtDateFrom.setVisibility(View.GONE);
            binding.dateLabelTo.setVisibility(View.GONE);
            binding.txtDateTo.setVisibility(View.GONE);
        }

        filterSearchVM.init(this, binding);
        filterSearchVM.getData(new Intent().putExtra(Constants.CATEGORY_ID, intent.getStringExtra(Constants.CATEGORY_ID)), subcategoriesManager);

        setupViews();

        binding.categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.subcategoriesList.setLayoutManager(new GridLayoutManager(this, 3));
        binding.subcategoriesList.setNestedScrollingEnabled(false);
        binding.categoriesRecyclerView.setNestedScrollingEnabled(false);
        getCategories();

        binding.toolbar.toolbarLeftIcon.setOnClickListener(t -> {
            onBackPressed();
        });
        binding.searchPriceRangebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i);

                float priceRound = Math.round(i);
                finalPrice = String.format("%.2f", priceRound);

                binding.textViewValue.setText(getString(R.string.value_up_to) + " R$ " + finalPrice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.searchPriceRangebar.setProgress(binding.searchPriceRangebar.getMax());

        txtDateFrom.setOnClickListener(v -> setupDatePicker(Constants.ID_FROM));
        txtDateTo.setOnClickListener(v -> setupDatePicker(Constants.ID_TO));

        if (filterDTO != null) {

            if (filterDTO.getStartCalendar() != null) {
                Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                instance.setTime(new Date(filterDTO.getStartCalendar()));
                calendarStartDateLong = instance.getTimeInMillis();

                int realMonth = instance.get(Calendar.MONTH) + 1;
                String dateString = instance.get(Calendar.DAY_OF_MONTH) + "/" + realMonth + "/" + instance.get(Calendar.YEAR);
                binding.txtDateFrom.setText(dateString);
                txtDateFrom.setBackground(getResources().getDrawable(R.drawable.not_too_round_background_grapefruit_filled));
                txtDateFrom.setTextColor(getResources().getColor(R.color.white));
            }

            if (filterDTO.getEndCalendar() != null) {
                Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                instance.setTime(new Date(filterDTO.getEndCalendar()));
                calendarEndDateLong = instance.getTimeInMillis();
                int realMonth = instance.get(Calendar.MONTH) + 1;
                String dateString = instance.get(Calendar.DAY_OF_MONTH) + "/" + realMonth + "/" + instance.get(Calendar.YEAR);
                binding.txtDateTo.setText(dateString);
                txtDateTo.setBackground(getResources().getDrawable(R.drawable.not_too_round_background_grapefruit_filled));
                txtDateTo.setTextColor(getResources().getColor(R.color.white));
            }

            binding.searchPriceRangebar.setProgress((int) filterDTO.getPriceRange());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("categories", "");
        outState.putString("subcategories", "");
        outState.putString("dateFrom", "");
        outState.putString("dateTo", "");
        outState.putInt("state", stateSelected);
        outState.putString("city", cityFilter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String restored = savedInstanceState.getString("state");

        txtDateFrom.setText(restored);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setupDatePicker(int id) {

        try {
            openCalendar(id);
        } catch (Exception e) {
            DialogUtils.INSTANCE.show(this, "Calendário indisponível!");
        }

    }

    private void openCalendar(int id) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case 0:

                dateSetListener = (view, year1, month1, dayOfMonth) -> {

                    Calendar calendarNow = Calendar.getInstance();
                    calendarNow.setTimeZone(TimeZone.getTimeZone("GMT"));
                    calendarNow.set(Calendar.AM_PM, Calendar.AM);
                    calendarNow.set(Calendar.YEAR, year1);
                    calendarNow.set(Calendar.MONTH, month1);
                    calendarNow.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendarNow.set(Calendar.HOUR, 6);
                    calendarNow.set(Calendar.MINUTE, 00);
                    calendarNow.set(Calendar.SECOND, 00);
                    calendarNow.set(Calendar.MILLISECOND, 00);

                    calendarStartDateLong = calendarNow.getTimeInMillis();

                    txtDateFrom.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                    txtDateFrom.setBackground(getResources().getDrawable(R.drawable.not_too_round_background_grapefruit_filled));
                    txtDateFrom.setTextColor(getResources().getColor(R.color.white));


                    if (!txtDateTo.toString().equals("") && !txtDateFrom.toString().equals("")) {
                        txtDateTo.setText("");
                        txtDateTo.setHint("DD/MM/AAAAA");
                        txtDateTo.setBackground(getResources().getDrawable(R.drawable.not_too_round_background_white));
                        txtDateTo.setTextColor(getResources().getColor(R.color.white));
                    }

                };
                break;

            case 1:
                dateSetListener = (view, year1, month1, dayOfMonth) -> {

                    Calendar calendarNow = Calendar.getInstance();
                    calendarNow.setTimeZone(TimeZone.getTimeZone("GMT"));
                    calendarNow.set(Calendar.AM_PM, Calendar.AM);
                    calendarNow.set(Calendar.YEAR, year1);
                    calendarNow.set(Calendar.MONTH, month1);
                    calendarNow.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendarNow.set(Calendar.HOUR, 6);
                    calendarNow.set(Calendar.MINUTE, 00);
                    calendarNow.set(Calendar.SECOND, 00);
                    calendarNow.set(Calendar.MILLISECOND, 00);

                    calendarEndDateLong = calendarNow.getTimeInMillis();

                    if (calendarStartDateLong != null && calendarEndDateLong < calendarStartDateLong) {
                        DialogUtils.INSTANCE.show(this, getString(R.string.invalid_date));

                    } else {
                        txtDateTo.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                        txtDateTo.setBackground(getResources().getDrawable(R.drawable.not_too_round_background_grapefruit_filled));
                        txtDateTo.setTextColor(getResources().getColor(R.color.white));
                    }
                };
                break;
        }
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(
                this, dateSetListener, year, month, day);


        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();

    }

    @OnClick(br.com.R2.id.buttonClear)
    public void clearSearchFilter() {
        if (categoriesList.size() > 0) {
            for (SelectableCircleComponentFIlter ca : categoriesList) {
                ca.updateImage(false);
            }
        }

        if (subCategoriesList.size() > 0) {
            filterSearchVM.cleansubcategory(subCategoriesList);
            binding.subcategoriesList.getAdapter().notifyDataSetChanged();
        }

        binding.searchPriceRangebar.setProgress(0);
        txtDateFrom.setText("");
        txtDateFrom.setHint("DD/MM/AAAA");
        txtDateFrom.setBackground(getResources().getDrawable(R.drawable.not_too_round_background_white));
        txtDateTo.setText("");
        txtDateTo.setHint("DD/MM/AAAA");
        txtDateTo.setBackground(getResources().getDrawable(R.drawable.not_too_round_background_white));

        filterDTO = new FilterDTO();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void getCategories() {
        SelectableCircleComponentFIlter emptyCircleComponent = new SelectableCircleComponentFIlter("", "", "", "", false);
        for (int i = 0; i < 10; i++) {
            categoriesList.add(emptyCircleComponent);
        }
        disposable = CategoriesInteractorImpl.Companion.getInstance().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    List<SelectableCircleComponentFIlter> components = new ArrayList<>();
                    for (Category categoryModel : categories) {

                        SelectableCircleComponentFIlter component = new SelectableCircleComponentFIlter(categoryModel.getImage(), categoryModel.getName(), categoryModel.getId(), categoryModel.getImageWhite(), selectedCategorie.equals(categoryModel.getId()));
                        component.isEvent = !(categoryModel.getType() != null && categoryModel.getType().equals(Category.Type.place));

                        components.add(component);
                    }

                    categoriesList.clear();
                    categoriesList.addAll(components);
                }, throwable -> {

                });
        // getCategoriesErrorTV.setVisibility(View.GONE);
    }

    void setupManagers() {
        subcategoriesManager = new SubcategoriesManager(this);
        subcategoriesManager.subcategoriesRequestHandler = this;
    }

    void setupViews() {

        TypefaceUtil.initilize(this);
        TypefaceUtil.mediumFont(binding.textViewOrderBy, binding.radioButtonDistance, binding.radioButtonRating,
                binding.textViewCategory, binding.subcategoryTitleText, binding.textViewlabelPrice,
                binding.imageViewSeeMore);

        noSubcategoriesMessage.set(false);
        binding.setActivity(this);
        isLoading.set(true);
    }

    @Override
    public void onClickToolbarLeftIcon() {

    }

    @Override
    public void onReceiveSubcategories(List<SubcategoriesModel> subcategoriesList) {
        subCategoriesList = subcategoriesList;

        if (subcategoriesList.isEmpty()) {
            noSubcategoriesMessage.set(true);
            isLoading.set(false);
            binding.subcategoriesList.setVisibility(View.GONE);

        } else {
            noSubcategoriesMessage.set(false);
            isLoading.set(false);
            binding.subcategoriesList.setVisibility(View.VISIBLE);

            String subcategoryId = null;
            if (filterDTO != null) {
                subcategoryId = filterDTO.getSubcategoryId();
            }

            filterSearchVM.setSubcategoryComponents(subcategoriesList, subcategoryId);
        }
    }

    @Override
    public void onFailureToReceiveSubcategories(String message) {
        isLoading.set(false);
        noSubcategoriesMessage.set(true);
    }

    public void hideKeyboard(View view) {
        Utils.INSTANCE.hideKeyboard(view, this);
    }

    public void onSearchEvent(View view) {
        if (progressDialog == null) {
            progressDialog = DialogUtils.INSTANCE.createProgressDialog(this);
        }
        progressDialog.show();
        if (categoryType != null) {
            final Bundle data = new Bundle();
            FilterSearchVM.ReturnFilters filters = filterSearchVM.getFilters(binding);

            FilterDTO filterDTO = new FilterDTO();


            if (filters.categoryId != null) {
                data.putString(Constants.CATEGORY_ID, filters.categoryId);
                filterDTO.setCategoryId(filters.categoryId);
            }
            if (filters.subcategoryId != null) {
                data.putString(Constants.SUBCATEGORY_ID, filters.subcategoryId);
                filterDTO.setSubcategoryId(filters.subcategoryId);

            }
            if (filters.searchText != null) {
                data.putString(Constants.QUERY, filters.searchText);
                filterDTO.setQuery(filters.searchText);
            }
//            if (filters.priceRange != 0.0) {
//                    data.putFloat(Constants.PRICE_RANGE, filters.priceRange);
//            }

            filtersList = filterSearchVM.getFiltersList(binding);

            String selectedCategorieName = null;
            for (SelectableCircleComponentFIlter c : categoriesList) {
                if (c.id.equals(selectedCategorie)) {
                    selectedCategorieName = c.name.get();
                }
            }

            if (selectedCategorieName != null) {
                data.putString(Constants.CATEGORY_NAME, selectedCategorieName);
                filterDTO.setCategoryName(selectedCategorieName);
            }

            if (calendarStartDateLong != null) {
                data.putLong(Constants.START_DATE_CALENDAR, calendarStartDateLong);
                filterDTO.setStartCalendar(calendarStartDateLong);

                if (calendarEndDateLong == null) {
                    data.putLong(Constants.END_DATE_CALENDAR, calendarStartDateLong);
                    filterDTO.setEndCalendar(calendarStartDateLong);
                }
            }

            if (calendarEndDateLong != null) {
                data.putLong(Constants.END_DATE_CALENDAR, calendarEndDateLong);
                filterDTO.setEndCalendar(calendarEndDateLong);

                if (calendarStartDateLong == null) {
                    data.putLong(Constants.START_DATE_CALENDAR, calendarEndDateLong);
                    filterDTO.setStartCalendar(calendarEndDateLong);
                }
            }

            if (finalPrice != null) {
                data.putString(Constants.PRICE_RANGE, finalPrice);
                filterDTO.setPriceRange(finalPrice);
            }

            if (cityFilter != null) {
                data.putString(Constants.CITY_FILTER, cityFilter);
                filterDTO.setCity(cityFilter);
            }

            if (stateFilter != null) {
                data.putString(Constants.STATE_FILTER, stateFilter);
                filterDTO.setState(stateFilter);
            }

            System.out.println("Data intent: " + data.toString());
            data.putSerializable("filterDTO", filterDTO);

            Coordinator.goToSearchClubResultsFinish(this, data, true, filtersList, filterSearchVM.categoryId, categoryType);
            NewFilterSearchActivity.this.finish();

        }
        progressDialog.hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    @Override
    public void onSelectCircle(SelectableCircleComponentFIlter component) {
        selectedCategorie = component.id;

        for (SelectableCircleComponentFIlter ca : categoriesList) {
            ca.updateImage(ca.equals(component));

            if (!component.isEvent) {
                binding.textViewlabelPrice.setVisibility(View.GONE);
                binding.searchPriceRangebar.setVisibility(View.GONE);
                binding.textViewValue.setVisibility(View.GONE);

                binding.textDateLabel.setVisibility(View.GONE);
                binding.dateLabelFrom.setVisibility(View.GONE);
                binding.txtDateFrom.setVisibility(View.GONE);
                binding.dateLabelTo.setVisibility(View.GONE);
                binding.txtDateTo.setVisibility(View.GONE);
            } else {
                binding.textViewlabelPrice.setVisibility(View.VISIBLE);
                binding.searchPriceRangebar.setVisibility(View.VISIBLE);
                binding.textViewValue.setVisibility(View.VISIBLE);

                binding.textDateLabel.setVisibility(View.VISIBLE);
                binding.dateLabelFrom.setVisibility(View.VISIBLE);
                binding.txtDateFrom.setVisibility(View.VISIBLE);
                binding.dateLabelTo.setVisibility(View.VISIBLE);
                binding.txtDateTo.setVisibility(View.VISIBLE);
            }
        }

        component.updateImage(true);
        filterSearchVM.init(this, binding);
        filterSearchVM.getData(new Intent().putExtra(Constants.CATEGORY_ID, component.id), subcategoriesManager);
    }
}
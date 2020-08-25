package br.com.legacy.viewControllers.activitites;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.example.library.Utils;
import com.google.gson.Gson;

import br.com.databinding.ActivityInviteFriendsOldBinding;
import br.com.legacy.adapters.FiltersChipAdapter;
import br.com.legacy.adapters.ItemAvatarTextAdapter;
import br.com.goin.component.session.user.UserModel;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.FriendsManager;
import br.com.legacy.models.ItemAvatarTextModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.ToolbarConfigurator;

import java.util.ArrayList;
import java.util.List;

public class SelectFriendsActivity extends AppCompatActivity implements FriendsManager.FriendRequestHandler{

    ActivityInviteFriendsOldBinding binding;
    RecyclerView friendsRecyclerView;
    RecyclerView selectedRecyclerView;
    EditText searchEditText;
    View toolbar;
    ProgressBar friendsListLoading;
    TextView emptyListPlaceholder;
    View container;
    Button nextButton;
    ImageView searchBarButton;

    FriendsManager friendsManager;
    ItemAvatarTextAdapter friendsOptionsAdapter;
    FiltersChipAdapter selectedFriendsAdapter;

    List<ItemAvatarTextModel> friendsOptionsList = new ArrayList<>();
    ArrayList<ItemAvatarTextModel> selectedFriendsList = new ArrayList<>();
    List<String> selectedNamesList = new ArrayList<>();
    ArrayList<String> selectedIdsList = new ArrayList<>();
    FiltersChipAdapter.OnRemoveListener removeListener;
    SelectFriendsType type;
    List<UserModel> usersOptions = new ArrayList<>();

    public enum SelectFriendsType {
        CreateGroup, BuyTickets
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friends_old);
        findViews();
        ToolbarConfigurator.INSTANCE.configToolbar(toolbar,getString(R.string.invite_friends),R.drawable.ic_arrow_back,0);
        if(getIntent().getSerializableExtra(Constants.TYPE) != null){
            type = (SelectFriendsType) getIntent().getSerializableExtra(Constants.TYPE);
        } else {
            type = SelectFriendsType.CreateGroup;
        }
        Coordinator.setStatusBarColor(this,null);
        setupChips();
        setupIntegration();
        setListeners();
        getData();
    }

    void findViews(){
        friendsRecyclerView = binding.friendsList;
        selectedRecyclerView = binding.invitedFriendsList;
        searchEditText = binding.inviteSearchBar.searchBarText;
        searchBarButton = binding.inviteSearchBar.searchBarIcon;
        toolbar = binding.inviteFriendsToolbar.getRoot();
        friendsListLoading = binding.friendsListLoading;
        emptyListPlaceholder = binding.eventNoFriendsToInvite;
        container = binding.inviteFriendsContainer;
        nextButton = binding.nextButton;
        nextButton.setVisibility(View.VISIBLE);
        selectedRecyclerView.setVisibility(View.VISIBLE);
        searchEditText.setHint(R.string.search);
    }

    void getData(){
        friendsManager.getFriends(searchEditText.getText().toString());
    }

    void setupIntegration(){
        friendsOptionsAdapter = new ItemAvatarTextAdapter(friendsOptionsList);
        selectedFriendsAdapter = new FiltersChipAdapter(selectedNamesList,removeListener, ContextCompat.getColor(this, R.color.gray));
        friendsRecyclerView.setAdapter(friendsOptionsAdapter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedRecyclerView.setAdapter(selectedFriendsAdapter);
        friendsManager = new FriendsManager(this);
    }

    void setupChips(){
        ChipsLayoutManager chipsSelectedFriendsLayoutManager = setupChipsLayout();
        selectedRecyclerView.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen._5sdp),
                getResources().getDimensionPixelOffset(R.dimen._5sdp)));
        selectedRecyclerView.setLayoutManager(chipsSelectedFriendsLayoutManager);
        removeListener = new FiltersChipAdapter.OnRemoveListener() {
            @Override
            public void onItemRemoved(int position) {
                deselectFriend(position);
            }
        };
    }

    ChipsLayoutManager setupChipsLayout(){
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.CENTER_HORIZONTAL)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true)
                //set maximum views count in a particular row
                .setMaxViewsInRow(3)
                //set gravity resolver where you can determine gravity for item_success_dialog in position.
                //This method have priority over previous one
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER_HORIZONTAL;
                    }
                })
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build();
        return chipsLayoutManager;
    }

    @Override
    public void onReceiveFriendsList(List<UserModel> friends) {

        System.out.println("Lista de amigos: "+friends);

        friendsListLoading.setVisibility(View.GONE);
        selectedRecyclerView.setVisibility(View.VISIBLE);
        if(friends.isEmpty()){
            emptyListPlaceholder.setVisibility(View.VISIBLE);
        } else {
            emptyListPlaceholder.setVisibility(View.GONE);
        }
        friendsOptionsList = mapUsersListToItemsList(friends);
        friendsOptionsAdapter.setItems(friendsOptionsList);
        this.usersOptions = friends;
    }

    @Override
    public void onFailureToReceiveFriendsList(String error) {
        friendsListLoading.setVisibility(View.GONE);
    }

    ItemAvatarTextModel mapUserToItemAvatarTextModel(UserModel user){
        ItemAvatarTextModel item = new ItemAvatarTextModel(this,user.getProfilePicture(),user.getName(),user.getId());
        item.userModel = user;
        item.setHandler(new ItemAvatarTextModel.ItemAvatarTextHandler() {
            @Override
            public void onClickItem(ItemAvatarTextModel item) {
                selectFriend(item);
            }

            @Override
            public void onClickAvatar(ItemAvatarTextModel item) {
                onClickItem(item);
            }

            @Override
            public void onClickText(ItemAvatarTextModel item) {
                onClickItem(item);
            }
        });
        if(selectedFriendsList.contains(item)){
            item.setSelected(true);
        }
        return item;
    }

    List<ItemAvatarTextModel> mapUsersListToItemsList(List<UserModel> users){
        List<ItemAvatarTextModel> list = new ArrayList<>();
        for(UserModel user: users){
            list.add(mapUserToItemAvatarTextModel(user));
        }

        return list;
    }

    void selectFriend(ItemAvatarTextModel itemAvatarTextModel){
        if(!selectedFriendsList.contains(itemAvatarTextModel)){
            itemAvatarTextModel.setSelected(true);
            selectedFriendsList.add(itemAvatarTextModel);
            selectedNamesList.add(itemAvatarTextModel.text);
            selectedIdsList.add(itemAvatarTextModel.id.toString());
            selectedFriendsAdapter.notifyItemInserted(selectedFriendsList.size() - 1);
        }
    }

    void deselectFriend(int position){
        int friendsListIndex = friendsOptionsList.indexOf(selectedFriendsList.get(position));
        if(friendsListIndex >= 0 && friendsListIndex <= friendsOptionsList.size() -1){
            friendsOptionsList.get(friendsListIndex).setSelected(false);
        }
        selectedFriendsList.remove(position);
        selectedNamesList.remove(position);
        selectedIdsList.remove(position);
        selectedFriendsAdapter.notifyItemRemoved(position);
    }

    void setListeners(){
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Utils.hideKeyboard(getCurrentFocus(),SelectFriendsActivity.this);
                    getData();
                    return true;
                }
                return false;
            }
        });

        searchBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals(SelectFriendsType.CreateGroup)){
                    if(selectedIdsList.isEmpty()){
                        DialogUtils.INSTANCE.show(SelectFriendsActivity.this,R.string.empty_members_list);
                    }
                } else{
                    Intent intent = new Intent();
                    intent.putExtra(Constants.SELECTED_FRIENDS,(new Gson()).toJson(getUsersFromItems(selectedFriendsList)));
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });

        binding.inviteFriendsToolbar.toolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_CREATE_GROUP && resultCode == RESULT_OK){
            finish();
        }
    }

    ArrayList<UserModel> getUsersFromItems(List<ItemAvatarTextModel> items){
        ArrayList<UserModel> users = new ArrayList<>();
        for(ItemAvatarTextModel item: items){
            users.add(item.userModel);
        }
        return users;
    }
}

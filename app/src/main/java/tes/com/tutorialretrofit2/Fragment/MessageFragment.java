package tes.com.tutorialretrofit2.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tes.com.tutorialretrofit2.API.APIService;
import tes.com.tutorialretrofit2.API.APIUrl;
import tes.com.tutorialretrofit2.Model.Messages;
import tes.com.tutorialretrofit2.R;
import tes.com.tutorialretrofit2.helper.MessageAdapter;
import tes.com.tutorialretrofit2.helper.SharedPrefManager;

public class MessageFragment extends Fragment {

    private RecyclerView recyclerViewMessages;
    private RecyclerView.Adapter adapter;
    private String TAG = "MessageFragment_Message";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Messages");

        recyclerViewMessages = (RecyclerView) view.findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getActivity()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);


        Call<Messages> call = service.getMessages(SharedPrefManager.getInstance(getActivity()).getUser().getId());
        Log.i(TAG, "onResponse: "+service.getMessages(SharedPrefManager.getInstance(getActivity()).getUser().getId()).request().url().toString());
        call.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {
                Log.i(TAG, "onResponse: "+response.body().getMessages());
                adapter = new MessageAdapter(response.body().getMessages(), getActivity());
                recyclerViewMessages.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onResponse: "+t.getMessage());
            }
        });
    }
}
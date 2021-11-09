package com.kitesoft.tpkakaosearchapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    PlaceListRecyclerAdapter placeListRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView= view.findViewById(R.id.recyclerview);
        setPlaceListRecyclerAdapter();
    }


    public void setPlaceListRecyclerAdapter(){
        MainActivity ma= (MainActivity) getActivity();

        //아직 MainActivity의 파싱작업이 완료되지 않았다면 데이터가 없음.
        if(ma.searchLocalApiResponse==null) return;

        placeListRecyclerAdapter= new PlaceListRecyclerAdapter(getActivity(), ma.searchLocalApiResponse.documents);
        recyclerView.setAdapter(placeListRecyclerAdapter);
    }
}

package com.example.anon.miniproject1_cntn16;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter
        <SearchAdapter.SearchHolder> {

    SearchAdapter(ArrayList<ShortMyPlace> shortMyPlaces) {
        this.shortMyPlaces = shortMyPlaces;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_item, viewGroup, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder searchHolder, int i) {
        final ShortMyPlace myData = shortMyPlaces.get(i);
        searchHolder.tv_name.setText(myData.getName());
        searchHolder.tv_address.setText(myData.getAddress());
        // click on item
        searchHolder.setMyItemClickListener(new MyItemClickListener() {
            @Override
            public void onClick(View view) {
                // goi activity info de gui thong tin
                Intent intent = new Intent(view.getContext(), InfoActivity.class);

                // gui id
                intent.putExtra("ID", myData.getId());

                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shortMyPlaces.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_address;
        private MyItemClickListener myItemClickListener;

        SearchHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.rv_item_name);
            tv_address = itemView.findViewById(R.id.rv_item_address);

            // trick to enable click on recycler view
            itemView.setOnClickListener(this);
        }

        void setMyItemClickListener(MyItemClickListener myItemClickListener) {
            this.myItemClickListener = myItemClickListener;
        }

        @Override
        public void onClick(View v) {
            myItemClickListener.onClick(v);
        }
    }

    private ArrayList<ShortMyPlace> shortMyPlaces;
}

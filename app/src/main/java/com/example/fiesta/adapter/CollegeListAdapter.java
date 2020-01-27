package com.example.fiesta.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiesta.EventDisplayActivity;
import com.example.fiesta.R;
import com.example.fiesta.models.College;
import com.example.fiesta.models.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollegeListAdapter extends RecyclerView.Adapter<CollegeListAdapter.ProductViewHolder> implements Filterable {

    private Context context;
    private List<College> collegeList;
    private List<College> allCollegeList;

    public CollegeListAdapter(Context c, List<College> p) {
        Log.d("COLLG_ADAP", p.toString());
        this.context = c;
        this.collegeList = p;
        this.allCollegeList = new ArrayList<>(p);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(this.context).inflate(R.layout.college_display_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        holder.collegeName.setText("College Name: "+collegeList.get(position).getCollegeItemName());
        holder.FestName.setText("Fest Name: " +collegeList.get(position).getCollegeListFestName());
        holder.StartDate.setText("Date: "+collegeList.get(position).getCollegeListStartDate()+" - "+collegeList.get(position).getCollegeListEndDate());
        holder.DetailsView.setText("Details: "+collegeList.get(position).getCollegeListFestDetails());
        holder.festContact.setText("Contact No: "+collegeList.get(position).getCollegeContact());
        Picasso.get().load(collegeList.get(position).getCollegeImage()).into(holder.collegeListImage);
        
        holder.openfestDetailsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent eventpage = new Intent(context, EventDisplayActivity.class).putExtra("festid", collegeList.get(position)._getCollegeId());
                context.startActivity(eventpage);
            }
        });
    }


    @Override
    public int getItemCount() {
        return collegeList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<College> filteredCollege = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredCollege.addAll(allCollegeList);
                }
                else {
                    String filteredPattern = charSequence.toString().toLowerCase().trim();
                    for (College u : allCollegeList) {
                        if (u.getCollegeItemName().toLowerCase().contains(filteredPattern) || u.getCollegeListFestName().toLowerCase().contains(filteredPattern)) {
                            filteredCollege.add(u);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCollege;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                collegeList.clear();
                collegeList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView collegeListImage;
        TextView collegeName,FestName,festContact,StartDate,DetailsView;
        LinearLayout openfestDetailsLL;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            collegeListImage = itemView.findViewById(R.id.collegeListImage);
            collegeName = itemView.findViewById(R.id.collegeName);

            festContact = itemView.findViewById(R.id.festContact);
            FestName=itemView.findViewById(R.id.FestName);
            StartDate=itemView.findViewById(R.id.StartDate);
            DetailsView=itemView.findViewById(R.id.DetailsView);
            openfestDetailsLL = itemView.findViewById(R.id.openfestDetails);
        }
    }
}

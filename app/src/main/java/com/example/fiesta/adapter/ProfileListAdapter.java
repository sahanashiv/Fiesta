package com.example.fiesta.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiesta.EventDisplayActivity;
import com.example.fiesta.FestUpdateActivity;
import com.example.fiesta.R;
import com.example.fiesta.models.College;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProductViewHolder> {

    private Context context;
    private List<College> profileList;

    public ProfileListAdapter(Context c, List<College> p) {
        Log.d("COLLG_ADAP", p.toString());
        this.context = c;
        this.profileList = p;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileListAdapter.ProductViewHolder(LayoutInflater.from(this.context).inflate(R.layout.college_display_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileListAdapter.ProductViewHolder holder, final int position) {

        holder.collegeName.setText("College Name: "+profileList.get(position).getCollegeItemName());
        holder.FestName.setText("Fest Name: " +profileList.get(position).getCollegeListFestName());
        holder.StartDate.setText("Date: "+profileList.get(position).getCollegeListStartDate()+" - "+profileList.get(position).getCollegeListEndDate());
        holder.DetailsView.setText("Details: "+profileList.get(position).getCollegeListFestDetails());
        holder.festContact.setText("Contact No: "+profileList.get(position).getCollegeContact());

        Picasso.get().load(profileList.get(position).getCollegeImage()).into(holder.collegeListImage);


        holder.cllgPostMenuIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(context, "something cliskfasfasd", Toast.LENGTH_LONG).show();

                PopupMenu popup = new PopupMenu(context, holder.cllgPostMenuIB);
                popup.inflate(R.menu.college_post_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_menu : {
                                context.startActivity(new Intent(context, FestUpdateActivity.class).putExtra("festId", profileList.get(position)._getCollegeId()));
                                break;
                            }
                            case R.id.delete_menu: {
                                FirebaseFirestore.getInstance().collection("festdata")
                                        .document(profileList.get(position)._getCollegeId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                profileList.remove(position);
                                                notifyItemRemoved(position);
                                            }
                                        });
                                break;
                            }
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView collegeListImage;
        TextView collegeName,FestName,StartDate,DetailsView,festContact;
        LinearLayout openfestDetailsLL;
        ImageButton cllgPostMenuIB;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            collegeListImage = itemView.findViewById(R.id.collegeListImage);
            collegeName = itemView.findViewById(R.id.collegeName);
            FestName=itemView.findViewById(R.id.FestName);

            festContact = itemView.findViewById(R.id.festContact);
            StartDate=itemView.findViewById(R.id.StartDate);
            DetailsView=itemView.findViewById(R.id.DetailsView);
            openfestDetailsLL = itemView.findViewById(R.id.openfestDetails);
            cllgPostMenuIB = itemView.findViewById(R.id.cllgPostMenuIB);

            cllgPostMenuIB.setVisibility(View.VISIBLE);

        }
    }
}

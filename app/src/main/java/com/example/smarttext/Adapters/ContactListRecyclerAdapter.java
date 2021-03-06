package com.example.smarttext.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.smarttext.MainActivity;
import com.example.smarttext.MainChat;
import com.example.smarttext.R;
import com.example.smarttext.utils.Config;
import com.example.smarttext.utils.ContactData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactListRecyclerAdapter extends RecyclerView.Adapter<ContactListRecyclerAdapter.ViewHolder>{
    private Context mContext=null;
    private ArrayList<ContactData> data;
    public ContactListRecyclerAdapter(Context mContext,ArrayList<ContactData>data) {
        this.mContext=mContext;
        this.data=data;
       }
    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_contact_show,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.userName.setText(data.get(i).getName());
        viewHolder.phoneNumber.setText(data.get(i).getPhoneNo());
        viewHolder.setListener(i);
        //TODO 02: To Get Image Profile From Database And Show On Contact
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rootLayout;
        ImageView profileImage;
        TextView userName;
        TextView phoneNumber;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rootLayout=itemView.findViewById(R.id.modelContactRelativeLayoutRoot);
            profileImage=itemView.findViewById(R.id.modelContactProfileImage);
            userName=itemView.findViewById(R.id.modelContactName);
            phoneNumber=itemView.findViewById(R.id.modelContactPhoneNo);
        }
        public void setListener(final int i)
        {
            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
                    reference.child(Config.NODE_ALL_CONTACT).orderByChild(Config.NODE_PHONE_NO)
                            .equalTo(data.get(i).getPhoneNo()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                Intent inte=new Intent(mContext, MainChat.class);
                                inte.putExtra(Config.PHONE_NUMBER,data.get(i).getPhoneNo());
                                inte.putExtra(Config.USER_NAME,data.get(i).getName());
                                mContext.startActivity(inte);
                            }
                            else
                            {
                                //TODO: Starting Invite Activity
                                Log.d("Posion Of data ", "onDataChange: not exist");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }

}

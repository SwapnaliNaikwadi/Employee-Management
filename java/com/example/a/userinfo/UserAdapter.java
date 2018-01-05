package com.example.a.userinfo;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.example.a.userinfo.DatabaseHelper.STUD_ID;
import static com.example.a.userinfo.MyContentProvider.CONTENT_URI;

/**
 * Created by A on 20/11/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private List<Student> cartList;
    ClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, phone, address;
        public ImageView thumbnail;
        Button btnDelete;

        private WeakReference<ClickListener> listenerWeakReference;

        public MyViewHolder(View view, ClickListener listener) {
            super(view);
            name = view.findViewById(R.id.s_name);
            phone = view.findViewById(R.id.s_phone);
            address = view.findViewById(R.id.s_address);
            thumbnail = view.findViewById(R.id.s_image);
            btnDelete = view.findViewById(R.id.delete);

            listenerWeakReference = new WeakReference<>(listener);

            btnDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == btnDelete.getId()) {
                DatabaseHelper db = new DatabaseHelper(context);

                db.deleteStud(cartList.get(getAdapterPosition()));
                cartList.remove(cartList.get(getAdapterPosition()));
                notifyDataSetChanged();
            } else {
                //Row pressed
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.addToBackStack("ShowDetailsFragment");
            /*    transaction.setCustomAnimations(R.anim.slide_in_side, R.anim.slide_out_side,
                        R.anim.slide_in_side, R.anim.slide_out_side);
                transaction.replace(R.id.main_view,
                        ViewFragment.newInstance(cartList.get(getAdapterPosition())),
                        "ShowDetailsFragment").commit();
*/
            }

            listenerWeakReference.get().onPositionClicked(getAdapterPosition());
        }
    }






    public UserAdapter(Context context, List<Student> cartList, ClickListener clickListener) {
        this.context = context;
        this.cartList = cartList;
        this.listener=clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        MyViewHolder holder=new MyViewHolder(itemView,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Student item = cartList.get(position);
        holder.name.setText(item.getStudName());
        holder.phone.setText(item.getStudPhone());
        holder.address.setText(item.getStudAddress());

        Glide.with(context).load(item.getImage()).into(holder.thumbnail);
        //Picasso.with(context).load(item.getImage()).into(holder.thumbnail);




    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Student item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    /*public void deleteData()
    {
        int id=1;
        getContentResolver().delete(CONTENT_URI,STUD_ID+"="+id,null);
    }*/
}

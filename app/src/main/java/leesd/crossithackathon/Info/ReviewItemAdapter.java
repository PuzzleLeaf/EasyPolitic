package leesd.crossithackathon.Info;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.Fb.FbObject;
import leesd.crossithackathon.R;
import leesd.crossithackathon.model.DetailUserReview;

/**
 * Created by cmtyx on 2017-12-17.
 */

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.ViewHolder> {

    interface OnReviewItemRemoveListener {
        public void removeItem(DetailUserReview item);
    }

    private Context context;
    private LayoutInflater mInflater;
    private List<DetailUserReview> res;
    private OnReviewItemRemoveListener onReviewItemRemoveListener;

    public ReviewItemAdapter(Context context, List<DetailUserReview> res, OnReviewItemRemoveListener onReviewItemRemoveListener){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
        this.onReviewItemRemoveListener = onReviewItemRemoveListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if(res.size() == 0) {
            return 1;
        } else {
            return res.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ratingBarInit(holder.ratingBar);
        if(res.size() == 0) {
            holder.name.setText("관리자");
            holder.date.setText("00/00/00");
            holder.contents.setText("등록된 평가가 없습니다.");
            holder.ratingBar.setRating(5);
        } else {
            holder.name.setText(res.get(position).getUserName());
            holder.date.setText(res.get(position).getUserReviewTime());
            holder.contents.setText(res.get(position).getUserReview());
            holder.ratingBar.setRating(res.get(position).getUserStarRating());
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(res.size()!=0 && res.get(position).getUserKey().equals(FbObject.firebaseAuth.getCurrentUser().getUid())) {
                    Toast.makeText(context, "길게 누르면 삭제 됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(res.size()!=0) {
                    onReviewItemRemoveListener.removeItem(res.get(position));
                }
                return false;
            }
        });
    }

    private void ratingBarInit(RatingBar ratingBar) {
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.colorDarkBlue), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_rating) RatingBar ratingBar;
        @BindView(R.id.review_name) TextView name;
        @BindView(R.id.review_date) TextView date;
        @BindView(R.id.review_contents) TextView contents;
        @BindView(R.id.review_item) LinearLayout item;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
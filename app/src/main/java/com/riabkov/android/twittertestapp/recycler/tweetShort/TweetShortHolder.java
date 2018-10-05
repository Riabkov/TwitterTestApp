package com.riabkov.android.twittertestapp.recycler.tweetShort;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.utils.CustomClickableSpan;
import com.twitter.Extractor;
import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TweetShortHolder extends RecyclerView.ViewHolder {

    private TweetShort mTweetShort;

    @BindView(R.id.tweet_user_picture)
    CircleImageView mUserPicture;
    @BindView(R.id.tweet_user_name)
    TextView mUserName;
    @BindView(R.id.tweet_text)
    TextView mTweetText;
    @BindView(R.id.tweet_favorite)
    ImageButton mFavoriteButton;


    TweetShortHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTweet(TweetShort tweetShort, OnClickableEntityClicked entityClicked){
        mTweetShort = tweetShort;
        mUserName.setText(mTweetShort.getUser().name);
        //mTweetText.setText();

        String text = mTweetShort.getText();
        List<Integer> range = mTweetShort.getDisplayTextRange();
        if(range!=null){
            text = text.substring(mTweetShort.getDisplayTextRange().get(0), mTweetShort.getDisplayTextRange().get(1));
        }
        SpannableString spannableString = new SpannableString(text);


        TweetEntities entities = mTweetShort.getTweetEntities();

        List<HashtagEntity> hashtagEntities = entities.hashtags;
        List<MentionEntity> mentionEntities = entities.userMentions;
        List<UrlEntity> urlEntities = entities.urls;

        if(hashtagEntities != null && !hashtagEntities.isEmpty()){
            for(HashtagEntity entity: hashtagEntities){
                spannableString.setSpan(newClickableSpan("#"+entity.text, entityClicked), entity.indices.get(0), entity.indices.get(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if(mentionEntities != null && !mentionEntities.isEmpty()){
            for(MentionEntity entity: mentionEntities){
                spannableString.setSpan(newClickableSpan("@"+entity.screenName, entityClicked), entity.indices.get(0), entity.indices.get(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }

        if(urlEntities != null && !urlEntities.isEmpty()){
            for(UrlEntity entity: urlEntities){
                spannableString.setSpan(newUrlClickableSpan(entity.expandedUrl), entity.indices.get(0), entity.indices.get(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        mTweetText.setText(spannableString);
        mTweetText.setMovementMethod(LinkMovementMethod.getInstance());
        mTweetText.setHighlightColor(Color.BLUE);

        Glide.with(mUserPicture)
                .load(mTweetShort.getUser().profileImageUrl)
                .into(mUserPicture);
    }

    private CustomClickableSpan newClickableSpan(String clickableText, OnClickableEntityClicked entityClicked){
        return new CustomClickableSpan(clickableText) {
            @Override
            public void onTextClick(String text, View view) {
                entityClicked.onClicked(text);
                Toast.makeText(mUserName.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private CustomClickableSpan newUrlClickableSpan(String clickableText){
        return new CustomClickableSpan(clickableText) {
            @Override
            public void onTextClick(String text, View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(text));
                view.getContext().startActivity(intent);
            }
        };
    }

    public interface OnClickableEntityClicked{
        void onClicked(String text);
    }
}

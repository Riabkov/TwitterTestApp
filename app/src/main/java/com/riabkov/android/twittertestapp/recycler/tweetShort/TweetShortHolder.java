package com.riabkov.android.twittertestapp.recycler.tweetShort;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.utils.CustomClickableSpan;
import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
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
    EmojiTextView mTweetText;
    @BindView(R.id.tweet_favorite)
    ImageButton mFavoriteButton;


    TweetShortHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTweet(TweetShort tweetShort, OnClickableEntityClicked entityClicked, OnFavoriteClicked onFavoriteClicked){
        mTweetShort = tweetShort;
        mUserName.setText(mTweetShort.getUser().name);

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

        List<String> emojis = EmojiParser.extractEmojis(mTweetShort.getText());
        List<Integer> emojis_start = new ArrayList<>();
        for(String emoji: emojis){
            emojis_start.add(text.indexOf(emoji));
        }

        if(hashtagEntities != null && !hashtagEntities.isEmpty()){
            for(HashtagEntity entity: hashtagEntities){
                int offset = findOffset(emojis_start, entity.indices.get(0));
                int code_start = entity.indices.get(0)+offset;
                int code_end = entity.indices.get(1)+offset;

                spannableString.setSpan(newClickableSpan("#"+entity.text, entityClicked), code_start, code_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if(mentionEntities != null && !mentionEntities.isEmpty()){
            for(MentionEntity entity: mentionEntities){
                int offset = findOffset(emojis_start, entity.indices.get(0));
                int code_start = entity.indices.get(0)+offset;
                int code_end = entity.indices.get(1)+offset;
                spannableString.setSpan(newClickableSpan("@"+entity.screenName, entityClicked), code_start, code_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }

        if(urlEntities != null && !urlEntities.isEmpty()){
            for(UrlEntity entity: urlEntities){
                int offset = findOffset(emojis_start, entity.indices.get(0));
                int code_start = entity.indices.get(0)+offset;
                int code_end = entity.indices.get(1)+offset;
                spannableString.setSpan(newUrlClickableSpan(entity.expandedUrl), code_start, code_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        mTweetText.setMovementMethod(LinkMovementMethod.getInstance());
        mTweetText.setHighlightColor(Color.BLUE);
        mTweetText.setText(spannableString);


        setFavoriteButton(mTweetShort.isFavorite());
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTweetShort.setFavorite(!mTweetShort.isFavorite());
                onFavoriteClicked.favoriteClicked(mTweetShort);
                setFavoriteButton(mTweetShort.isFavorite());
            }
        });

        Glide.with(mUserPicture)
                .load(mTweetShort.getUser().profileImageUrl)
                .into(mUserPicture);
    }

    private CustomClickableSpan newClickableSpan(String clickableText, OnClickableEntityClicked entityClicked){
        return new CustomClickableSpan(clickableText) {
            @Override
            public void onTextClick(String text, View view) {
                entityClicked.onClicked(text);
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

    public interface OnFavoriteClicked{
        void favoriteClicked(TweetShort tweet);
    }

    private void setFavoriteButton(boolean isFavorite){
        Resources resources = mFavoriteButton.getResources();
        if(isFavorite){
            mFavoriteButton.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_selected));
        }else{
            mFavoriteButton.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_unselected));
        }
    }

    private int findOffset(List<Integer> emoji_starts, int char_start){
        int offset = 0;
        for(int i = 0; i<emoji_starts.size(); i++){
            if(emoji_starts.get(i)>char_start){
                break;
            }
            offset+= 1;
        }
        return offset;
    }
}

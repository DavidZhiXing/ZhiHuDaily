
package com.sdust.zhihudaily.data.source.remote;

import com.sdust.zhihudaily.data.model.Comments;
import com.sdust.zhihudaily.data.model.DailyStories;
import com.sdust.zhihudaily.data.model.StartImage;
import com.sdust.zhihudaily.data.model.Story;
import com.sdust.zhihudaily.data.model.StoryExtra;
import com.sdust.zhihudaily.data.model.Theme;
import com.sdust.zhihudaily.data.model.Themes;


public interface NetRepository {
	void getStartImage(int width,int height,Callback<StartImage> callback);

	void getThemes(Callback<Themes> callback);

	void getLatestDailyStories(Callback<DailyStories> callback);

	void getBeforeDailyStories(String date, Callback<DailyStories> callback);

	void getThemeStories(String themeId, Callback<Theme> callback);

	void getBeforeThemeStories(String themeId, String storyId, Callback<Theme> callback);

	void getStoryDetail(String storyId, Callback<Story> callback);

	void getStroyExtra(String storyId, Callback<StoryExtra> callback);

	void getLongComment(String storyId,Callback<Comments> callback);

	void getShortComment(String storyId,Callback<Comments> callback);

	void getLongCommentBefore(String storyId,String id,Callback<Comments> callback);

	void getShortCommentBefore(String storyId,String id,Callback<Comments> callback);



	public interface Callback<T> {
		public void success(T t,String url);
		public void failure(Exception e,String url);
	}
}

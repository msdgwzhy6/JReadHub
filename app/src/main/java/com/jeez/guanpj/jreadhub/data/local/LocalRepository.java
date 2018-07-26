package com.jeez.guanpj.jreadhub.data.local;

import android.support.annotation.NonNull;

import com.jeez.guanpj.jreadhub.bean.BaseListItemBean;
import com.jeez.guanpj.jreadhub.bean.NewsBean;
import com.jeez.guanpj.jreadhub.bean.TopicBean;
import com.jeez.guanpj.jreadhub.data.local.dao.NewsDao;
import com.jeez.guanpj.jreadhub.data.local.dao.TopicDao;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalRepository implements LocalDataSource {

    private NewsDao mNewsDao;
    private TopicDao mTopicDao;
    private Executor mExecutor;

    @Inject
    public LocalRepository(TopicDao topicDao, NewsDao newsDao, Executor executor) {
        mTopicDao = topicDao;
        mNewsDao = newsDao;
        mExecutor = executor;
    }

    @Override
    public Flowable<List<TopicBean>> getTopicById(String id) {
        return mTopicDao.getTopicById(id);
    }

    @Override
    public Flowable<List<NewsBean>> getNewsById(String id) {
        return mNewsDao.getNewsById(id);
    }

    @Override
    public <T> Single<T> getSingleBean(Class<T> tClass, String id) {
        if (TopicBean.class.equals(tClass)) {
            return (Single<T>) mTopicDao.getSingleTopicById(id);
        } else if (NewsBean.class.equals(tClass)) {
            return (Single<T>) mNewsDao.getSingleNewsById(id);
        }
        return null;
    }

    @Override
    public Flowable<List<TopicBean>> getTopicsByKeyword(@NonNull String keyWord) {
        return mTopicDao.getTopicsByKeyword(keyWord);
    }

    @Override
    public Flowable<List<NewsBean>> getNewsByKeyword(@NonNull String keyWord) {
        return mNewsDao.getNewsByKeyword(keyWord);
    }

    @Override
    public Flowable<List<TopicBean>> getAllTopic() {
        return  mTopicDao.getAllTopic();
    }

    @Override
    public Flowable<List<NewsBean>> getAllNews() {
        return  mNewsDao.getAllNews();
    }

    @Override
    public void delete(@NonNull BaseListItemBean object) {
        mExecutor.execute(() -> {
            if (object instanceof TopicBean) {
                mTopicDao.deleteTopic((TopicBean) object);
            } else if (object instanceof NewsBean) {
                mNewsDao.deleteNews((NewsBean) object);
            }
        });
    }

    @Override
    public void insert(@NonNull BaseListItemBean object) {
        mExecutor.execute(() -> {
            if (object instanceof TopicBean) {
                mTopicDao.insertTopic((TopicBean) object);
            } else if (object instanceof NewsBean) {
                mNewsDao.insertNews((NewsBean) object);
            }
        });
    }

    @Override
    public void update(@NonNull BaseListItemBean object) {
        mExecutor.execute(() -> {
            if (object instanceof TopicBean) {
                mTopicDao.updateTopic((TopicBean) object);
            } else if (object instanceof NewsBean) {
                mNewsDao.updateNews((NewsBean) object);
            }
        });
    }
}

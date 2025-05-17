package com.example.dartscoreboard;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.dartscoreboard.application.data.local.Database;
import com.example.dartscoreboard.match.data.local.MatchDao;
import com.example.dartscoreboard.match.data.local.StatsDao;
import com.example.dartscoreboard.match.data.local.UserDao;
import com.example.dartscoreboard.match.data.models.Leg;
import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchSettings;
import com.example.dartscoreboard.match.data.models.MatchType;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.match.data.models.User;
import com.example.dartscoreboard.match.data.models.Visit;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private UserDao userDao;
    private StatsDao statsDao;
    private MatchDao matchDao;
    private Database db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room
                .inMemoryDatabaseBuilder(context, Database.class)
                .setTransactionExecutor(Executors.newSingleThreadExecutor())
                .build();
        matchDao = db.matchesDao();
        statsDao = db.statsDao();
        userDao = db.userDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsertUser() throws Exception {
        User user = new User("TEST");
        userDao.insertUser(user);
        List<User> users = LiveDataTestUtil.getOrAwaitValue(userDao.getAllUsers());
        //see also RxJava test?
        assert users != null;
        assertEquals("user size is 1", 1, users.size());
    }

    @Test
    public void testDeleteUser() throws InterruptedException {
        User user = new User("TEST");
        User user2 = new User("TEST2");
        Completable.fromAction(() -> userDao.insertUser(user))
                .andThen(Completable.fromAction(() -> userDao.insertUser(user2)))
                .andThen(Completable.fromAction(() -> userDao.deleteUser(user)))
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    List<User> users = LiveDataTestUtil.getOrAwaitValue(userDao.getAllUsers());
                    assertEquals("user size is 1", 1, users.size());
                });
    }

//    @Test
//    public void testInsertVisit() throws InterruptedException {
//        userDao.insertUser(new User("player"));
//
//        Match match = new Match("testMatchId", MatchType.FiveO, new MatchSettings(1,1), OffsetDateTime.now());
//        matchDao.insertMatch(match);
//
//
//        Set set = new Set("testSetId", match.matchId);
//        matchDao.insertSet(set);
//        matchDao.insertLeg(new Leg("testLegId", set.setId, match.matchId, 0));
//
//        Visit visit = new Visit();
//        visit.score = 60;
//        visit.legId = "testLegId";
//        visit.userId = 1;
//        matchDao.insertVisit(visit);
//
//        int count = LiveDataTestUtil.getOrAwaitValue(matchDao.countUserVisits("legId",1));
//        assertEquals("number of visits is 1", 1, count);
//    }
    

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();



//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
////        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
////        assertEquals("com.example.dartscoreboard", appContext.getPackageName());
//
//
//
//
//    }
}
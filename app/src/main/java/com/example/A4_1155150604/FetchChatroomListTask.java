package com.example.A4_1155150604;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FetchChatroomListTask extends AsyncTask<String, Void, Void> {
    private static String emptyChatroomList = "Server returns 0 chatrooms";
    private ArrayList<Chatroom> chatrooms;
    private ChatroomAdapter chatroomAdapter;
    private ListView lv;
    private Context context;

    public FetchChatroomListTask(ArrayList<Chatroom> chatrooms, ChatroomAdapter chatroomAdapter, ListView lv, Context context) {
        this.chatrooms = chatrooms;
        this.chatroomAdapter = chatroomAdapter;
        this.lv = lv;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String json_result = Utils.fetchPage(strings[0]);
        if (json_result.equals("")) { //connection failed
            return null;
        }

        try {
            JSONObject json = new JSONObject(json_result);
            String status = json.getString("status");
            if (!status.equals("OK")) { //status error
                return null;
            }

            JSONArray chatroomArray = json.getJSONArray("data");
            for (int i = 0; i < chatroomArray.length(); i++) {
                String name = chatroomArray.getJSONObject(i).getString("name");
                int id = chatroomArray.getJSONObject(i).getInt("id");
                chatrooms.add(new Chatroom(name, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!chatrooms.isEmpty()) {
            chatroomAdapter.notifyDataSetChanged();
            lv.setSelection(chatrooms.size() - 1);
        } else {
            Toast.makeText(context, emptyChatroomList, Toast.LENGTH_SHORT).show();
        }
    }
}


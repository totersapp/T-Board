/*
 * Copyright 2019, Supermac
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toters.mykeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.toters.mykeyboard.recycler.SimpleRecyclerAdapter;
import com.toters.tboard.CustomKeyBoard;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;
    private List<String> stringList = new ArrayList<>();
    private CustomKeyBoard customKeyBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        customKeyBoard = new CustomKeyBoard(this,R.id.keyboardview);
        recyclerView = findViewById(R.id.recycler);

        simpleRecyclerAdapter = new SimpleRecyclerAdapter(customKeyBoard,generateList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(simpleRecyclerAdapter);
    }

    private List<String> generateList(){
        for (int i = 0; i < 10; i++) {
            stringList.add("Hello world");
        }
        return stringList;
    }
}

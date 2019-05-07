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

package com.toters.mykeyboard.recycler;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.toters.mykeyboard.R;
import com.toters.tboard.CustomKeyBoard;

import java.util.List;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder> {

    private List<String> stringList;
    private LayoutInflater layoutInflater;
    private CustomKeyBoard customKeyBoard;


    public SimpleRecyclerAdapter(CustomKeyBoard customKeyBoard, List<String> stringList) {
        this.customKeyBoard = customKeyBoard;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View rootView = layoutInflater.inflate(R.layout.simple_item_list, viewGroup, false);
        return new SimpleViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder simpleViewHolder, int i) {
        String s = stringList.get(i);
        simpleViewHolder.mTextView.setText(s);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder{
        private EditText editText;
        private TextView mTextView;
        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.tv_view);
            editText = itemView.findViewById(R.id.et_view);

            customKeyBoard.registerRecyclerEditText(editText, CustomKeyBoard.INPUT_TYPE_TEXT,R.xml.qwerty);
        }
    }
}

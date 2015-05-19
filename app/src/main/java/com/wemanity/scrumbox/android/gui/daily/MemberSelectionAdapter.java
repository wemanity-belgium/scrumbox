package com.wemanity.scrumbox.android.gui.daily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.db.entity.Participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemberSelectionAdapter extends BaseAdapter implements View.OnClickListener {
    private final LayoutInflater inflater;
    private final List<Member> memberParticipent;
    private final List<Member> members;
    int layoutId;
    int checkBoxId;
    public MemberSelectionAdapter(Context context, List<Member> members, int layoutId, int checkBoxId, List<Participant> participants){
        this(context,members,layoutId,checkBoxId);
        for(Participant participant : participants){
            memberParticipent.add(participant.getMember());
        }
    }
    public MemberSelectionAdapter(Context context, List<Member> members, int layoutId, int checkBoxId){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.members = members;
        this.layoutId = layoutId;
        this.checkBoxId = checkBoxId;
        memberParticipent = new ArrayList<>(members.size());
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return members.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = inflater.inflate(layoutId, parent, false);
        } else {
            v = convertView;
        }
        CheckBox checkBox = (CheckBox)v.findViewById(checkBoxId);
        checkBox.setTag(getItem(position));
        checkBox.setOnClickListener(this);

        bindView(position, v);

        return v;
    }

    private void bindView(int position, View v){
        TextView memberNickname = (TextView) v.findViewById(R.id.memberNickNameTextView);
        CheckBox checkBox = (CheckBox)v.findViewById(checkBoxId);
        Member member = (Member) getItem(position);
        if (memberNickname != null){
            memberNickname.setText(member.getNickname());
        }
        if (checkBox != null){
            checkBox.setChecked(memberParticipent.contains(member));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == checkBoxId){
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()){
               memberParticipent.add((Member)v.getTag());
            } else {
                memberParticipent.remove(v.getTag());
            }
        }
    }

    public List<Member> getMemberParticipent() {
        return Collections.unmodifiableList(memberParticipent);
    }
}

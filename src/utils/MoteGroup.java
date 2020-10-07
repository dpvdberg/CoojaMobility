package utils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class MoteGroup {
    private final String groupName;
    private final List<MobilityMote> moteList;

    public MoteGroup(String groupName) {
        this.groupName = groupName;
        moteList = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public List<MobilityMote> getMoteList() {
        return moteList;
    }

    public void addMote(MobilityMote mote) {
        moteList.add(mote);
    }

    public void addAll(Collection<MobilityMote> motes) {
        moteList.addAll(motes);
    }

    public void clear() {
        moteList.clear();
    }

    public void removeMote(MobilityMote mote) {
        moteList.remove(mote);
    }

    public void removeAll(Collection<MobilityMote> motes) {
        moteList.removeAll(motes);
    }

    @Override
    public String toString() {
        return groupName;
    }

    public DefaultMutableTreeNode getTreeNode() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
        moteList.sort(Comparator.comparingInt(MobilityMote::getID));
        for (MobilityMote mote : moteList) {
            node.add(new DefaultMutableTreeNode(mote));
        }
        return node;
    }
}

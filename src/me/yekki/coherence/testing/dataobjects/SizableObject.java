package me.yekki.coherence.testing.dataobjects;

import com.tangosol.util.ExternalizableHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SizableObject implements Externalizable{

    List<?> payload = new ArrayList<byte[]>();

    public SizableObject() {
    }

    public SizableObject(List<byte[]> payload) {
        this.payload = payload;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableHelper.writeCollection(out, payload);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        ExternalizableHelper.readCollection(in, payload, this.getClass().getClassLoader());
    }
}

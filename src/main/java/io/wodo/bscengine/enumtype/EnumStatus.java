package io.wodo.bscengine.enumtype;

public enum EnumStatus implements ValueEnum<Integer> {

    DELETED(-1),
    PASSIVE(0),
    ACTIVE(1);

    private final Integer value;

    EnumStatus(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
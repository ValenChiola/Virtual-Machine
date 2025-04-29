package models.functions.arithmetic;

import models.components.VM;
import models.functions.Mnemonic;

public abstract class Arithmetic extends Mnemonic {
  protected boolean overWritesA = true;

  public Arithmetic() {
    super();
  }

  @Override
  public void execute(int typeA, int typeB, int A, int B) throws Exception {
    VM vm = VM.getInstance();

    int AValue = vm.dataReadHandler(A, typeA);
    int BValue = vm.dataReadHandler(B, typeB);
    int result = getResult(AValue, BValue);
    if (overWritesA)
      vm.dataWriteHandler(A, result, typeA);
    setCC(result);
    afterWrite(AValue, BValue, result);
  }

  protected abstract int getResult(int AValue, int BValue);

  private void setCC(int value) {
    int result = 0;

    if (value < 0)
      result = 1 << 1;

    if (value == 0)
      result |= 1;

    result <<= 30;

    VM.getInstance().registers.get(8).setValue(result);
  }

  protected void afterWrite(int AValue, int BValue, int result) {
    // Default implementation does nothing
  }

}

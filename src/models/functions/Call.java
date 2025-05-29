package models.functions;

import models.functions.jumps.JMP;

public class Call extends Mnemonic {

  public Call() {
    super();
  }

  @Override
  public void execute(int typeB, int B) throws Exception {
    new Push().execute(1, 0x50); // pusheo el valor de IP
    new JMP().execute(typeB, B);
  }

}

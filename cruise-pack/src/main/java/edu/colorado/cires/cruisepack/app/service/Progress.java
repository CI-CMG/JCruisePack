package edu.colorado.cires.cruisepack.app.service;

public class Progress {

  private String status;
  private int totalFiles;
  private int copiedFiles;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getTotalFiles() {
    return totalFiles;
  }

  public void setTotalFiles(int totalFiles) {
    this.totalFiles = totalFiles;
  }

  public int getCopiedFiles() {
    return copiedFiles;
  }

  public void setCopiedFiles(int copiedFiles) {
    this.copiedFiles = copiedFiles;
  }
}

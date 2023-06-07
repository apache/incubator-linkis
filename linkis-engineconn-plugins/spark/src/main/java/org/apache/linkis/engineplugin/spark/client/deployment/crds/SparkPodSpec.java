package org.apache.linkis.engineplugin.spark.client.deployment.crds;


import java.util.List;
import java.util.Map;


public class SparkPodSpec {

  private Integer cores;

  private String coreLimit;

  private String memory;

  private Map<String, String> labels;

  private Map<String, String> annotations;

  private String serviceAccount;

  private List<VolumeMount> volumeMounts;

  private Integer instances;

  public Integer getCores() {
    return cores;
  }

  public void setCores(Integer cores) {
    this.cores = cores;
  }

  public String getCoreLimit() {
    return coreLimit;
  }

  public void setCoreLimit(String coreLimit) {
    this.coreLimit = coreLimit;
  }

  public String getMemory() {
    return memory;
  }

  public void setMemory(String memory) {
    this.memory = memory;
  }

  public Map<String, String> getLabels() {
    return labels;
  }

  public void setLabels(Map<String, String> labels) {
    this.labels = labels;
  }

  public Map<String, String> getAnnotations() {
    return annotations;
  }

  public void setAnnotations(Map<String, String> annotations) {
    this.annotations = annotations;
  }

  public String getServiceAccount() {
    return serviceAccount;
  }

  public void setServiceAccount(String serviceAccount) {
    this.serviceAccount = serviceAccount;
  }

  public List<VolumeMount> getVolumeMounts() {
    return volumeMounts;
  }

  public void setVolumeMounts(List<VolumeMount> volumeMounts) {
    this.volumeMounts = volumeMounts;
  }

  public Integer getInstances() {
    return instances;
  }

  public void setInstances(Integer instances) {
    this.instances = instances;
  }

  public static SparkPodSpecBuilder Builder() {
    return new SparkPodSpecBuilder();
  }


  public static  class SparkPodSpecBuilder {
    private Integer cores;
    private String coreLimit;
    private String memory;
    private Map<String, String> labels;
    private Map<String, String> annotations;
    private String serviceAccount;
    private List<VolumeMount> volumeMounts;
    private Integer instances;


    public Integer getCores() {
      return cores;
    }

    public void setCores(Integer cores) {
      this.cores = cores;
    }

    public String getCoreLimit() {
      return coreLimit;
    }

    public void setCoreLimit(String coreLimit) {
      this.coreLimit = coreLimit;
    }

    public String getMemory() {
      return memory;
    }

    public void setMemory(String memory) {
      this.memory = memory;
    }

    public Map<String, String> getLabels() {
      return labels;
    }

    public void setLabels(Map<String, String> labels) {
      this.labels = labels;
    }

    public Map<String, String> getAnnotations() {
      return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
      this.annotations = annotations;
    }

    public String getServiceAccount() {
      return serviceAccount;
    }

    public void setServiceAccount(String serviceAccount) {
      this.serviceAccount = serviceAccount;
    }

    public List<VolumeMount> getVolumeMounts() {
      return volumeMounts;
    }

    public void setVolumeMounts(List<VolumeMount> volumeMounts) {
      this.volumeMounts = volumeMounts;
    }

    public Integer getInstances() {
      return instances;
    }

    public void setInstances(Integer instances) {
      this.instances = instances;
    }

    private SparkPodSpecBuilder() {
    }



    public SparkPodSpecBuilder cores(Integer cores) {
      this.cores = cores;
      return this;
    }

    public SparkPodSpecBuilder coreLimit(String coreLimit) {
      this.coreLimit = coreLimit;
      return this;
    }

    public SparkPodSpecBuilder memory(String memory) {
      this.memory = memory;
      return this;
    }

    public SparkPodSpecBuilder labels(Map<String, String> labels) {
      this.labels = labels;
      return this;
    }

    public SparkPodSpecBuilder annotations(Map<String, String> annotations) {
      this.annotations = annotations;
      return this;
    }

    public SparkPodSpecBuilder serviceAccount(String serviceAccount) {
      this.serviceAccount = serviceAccount;
      return this;
    }

    public SparkPodSpecBuilder volumeMounts(List<VolumeMount> volumeMounts) {
      this.volumeMounts = volumeMounts;
      return this;
    }

    public SparkPodSpecBuilder instances(Integer instances) {
      this.instances = instances;
      return this;
    }

    public SparkPodSpec build() {
      SparkPodSpec sparkPodSpec = new SparkPodSpec();
      sparkPodSpec.annotations = this.annotations;
      sparkPodSpec.coreLimit = this.coreLimit;
      sparkPodSpec.instances = this.instances;
      sparkPodSpec.labels = this.labels;
      sparkPodSpec.serviceAccount = this.serviceAccount;
      sparkPodSpec.cores = this.cores;
      sparkPodSpec.memory = this.memory;
      sparkPodSpec.volumeMounts = this.volumeMounts;
      return sparkPodSpec;
    }
  }
}

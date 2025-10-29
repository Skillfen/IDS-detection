provider "kubernetes" {
  config_path = "~/.kube/config"
}

# Namespace pour Spring Boot
resource "kubernetes_namespace" "springboot_ns" {
  metadata {
    name = "springboot-ns"
  }
}

# Exemple de ConfigMap pour Spring Boot
resource "kubernetes_config_map" "springboot_config" {
  metadata {
    name      = "springboot-config"
    namespace = kubernetes_namespace.springboot_ns.metadata[0].name
  }

  data = {
    SPRING_DATASOURCE_URL = "jdbc:h2:mem:testdb"
    SPRING_DATASOURCE_USERNAME = "sa"
    SPRING_DATASOURCE_PASSWORD = "password"
  }
}

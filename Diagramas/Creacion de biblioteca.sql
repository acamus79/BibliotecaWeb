-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema biblioteca
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema biblioteca
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `biblioteca` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `biblioteca` ;

-- -----------------------------------------------------
-- Table `biblioteca`.`autor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`autor` (
  `id` VARCHAR(255) NOT NULL,
  `alta` BIT(1) NULL DEFAULT NULL,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_hrxnpr6qgsqoctag1ckjq2dcs` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `biblioteca`.`editorial`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`editorial` (
  `id` VARCHAR(255) NOT NULL,
  `alta` BIT(1) NULL DEFAULT NULL,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_eapnhl7c4en8ug7jfqahnyn8f` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `biblioteca`.`portada`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`portada` (
  `id` VARCHAR(255) NOT NULL,
  `contenido` LONGBLOB NULL DEFAULT NULL,
  `mime` VARCHAR(255) NULL DEFAULT NULL,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `biblioteca`.`libro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`libro` (
  `id` VARCHAR(255) NOT NULL,
  `alta` BIT(1) NULL DEFAULT NULL,
  `anio` INT NULL DEFAULT NULL,
  `ejemplares` INT NULL DEFAULT NULL,
  `ejemplares_prestados` INT NULL DEFAULT NULL,
  `ejemplares_restantes` INT NULL DEFAULT NULL,
  `isbn` VARCHAR(255) NULL DEFAULT NULL,
  `titulo` VARCHAR(255) NULL DEFAULT NULL,
  `autor_id` VARCHAR(255) NULL DEFAULT NULL,
  `editorial_id` VARCHAR(255) NULL DEFAULT NULL,
  `sinopsis` VARCHAR(400) NULL DEFAULT NULL,
  `portada_id` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `isbn_UNIQUE` (`isbn` ASC) VISIBLE,
  INDEX `FKe1ss87ymon6qj17bhr6jfh0c4` (`autor_id` ASC) VISIBLE,
  INDEX `FK79q7g2604hcmfdxw6ek3jt4el` (`editorial_id` ASC) VISIBLE,
  INDEX `FK2ua832tbhe5eokdhaftrfpf24` (`portada_id` ASC) VISIBLE,
  CONSTRAINT `FK2ua832tbhe5eokdhaftrfpf24`
    FOREIGN KEY (`portada_id`)
    REFERENCES `biblioteca`.`portada` (`id`),
  CONSTRAINT `FK79q7g2604hcmfdxw6ek3jt4el`
    FOREIGN KEY (`editorial_id`)
    REFERENCES `biblioteca`.`editorial` (`id`),
  CONSTRAINT `FKe1ss87ymon6qj17bhr6jfh0c4`
    FOREIGN KEY (`autor_id`)
    REFERENCES `biblioteca`.`autor` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `biblioteca`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`usuario` (
  `id` VARCHAR(255) NOT NULL,
  `alta` DATETIME(6) NULL DEFAULT NULL,
  `apellido` VARCHAR(255) NULL DEFAULT NULL,
  `baja` DATETIME(6) NULL DEFAULT NULL,
  `clave` VARCHAR(255) NULL DEFAULT NULL,
  `mail` VARCHAR(255) NOT NULL,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  `rol` VARCHAR(255) NULL DEFAULT NULL,
  `cantidad_prestamos` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `mail_UNIQUE` (`mail` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `biblioteca`.`prestamo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`prestamo` (
  `id` VARCHAR(36) NOT NULL,
  `alta` BIT(1) NULL DEFAULT NULL,
  `devolucion` DATETIME(6) NULL DEFAULT NULL,
  `prestamo` DATETIME(6) NULL DEFAULT NULL,
  `libro_id` VARCHAR(36) NULL DEFAULT NULL,
  `usuario_id` VARCHAR(36) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKk7uwwn2ov4su2plcn1jh4dbi0` (`libro_id` ASC) VISIBLE,
  INDEX `FKqxhq6d4w6fuv27c7j3af28wdu` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `FKk7uwwn2ov4su2plcn1jh4dbi0`
    FOREIGN KEY (`libro_id`)
    REFERENCES `biblioteca`.`libro` (`id`),
  CONSTRAINT `FKqxhq6d4w6fuv27c7j3af28wdu`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `biblioteca`.`usuario` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

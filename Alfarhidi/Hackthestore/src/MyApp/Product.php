<?php

namespace MyApp;

use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity
 * @ORM\Table(name="products")
 */
class Product
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="string", length=255)
     */
    private $product_name;

    /**
     * @ORM\Column(type="string", length=255, unique=true)
     */
    private $productID;


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getproduct_name(): ?string
    {
        return $this->product_name;
    }

    public function setproduct_name(string $product_name): self
    {
        $this->product_name = $product_name;

        return $this;
    }

    public function getProductID(): ?string
    {
        return $this->productID;
    }

    public function setProductID(string $productID): self
    {
        $this->productID = $productID;

        return $this;
    }
}

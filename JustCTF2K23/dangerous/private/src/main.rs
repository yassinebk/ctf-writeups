
use sha256::{digest, try_digest};
fn main() {
    let color_1: &str = "32cae2";
    let color_2: &str = "92e1e8";
    let mut ip_adresses_1: Vec<String> = vec![];
    let mut counter: i64 = 0;

    for a in 1..224 {
        for b in 0..256 {
            for c in 0..256 {
                for d in 1..255 {
                    let ip_address = format!("{}.{}.{}.{}", a, b, c, d);
                    let hashed_color_1 = digest(format!("{}1", ip_address.clone()));
                    let found_color_1_ip = hashed_color_1.starts_with(color_1);

                    if found_color_1_ip {
                        let hashed_color_2 = digest(format!("{}2", ip_address.clone()));
                        let found_color_2_ip = hashed_color_2.starts_with(color_2);
                        ip_adresses_1.push(ip_address.clone());
                        if found_color_2_ip {
                            println!("FOUND IP: {}", ip_address.clone());
                            return;
                        }
                    }

                    counter += 1;

                    if counter % 10_000_000 == 0 {
                        println!("IP adresses 1: {}", ip_adresses_1.len());
                        println!("Count: {}", counter);
                        println!("Current ip: {}", ip_address.clone());
                    }
                }
            }
        }
    }
}

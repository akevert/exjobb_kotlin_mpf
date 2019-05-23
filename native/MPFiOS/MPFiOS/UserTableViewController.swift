//
//  UserTableViewController.swift
//  MPFiOS
//
//  Created by Anna-Karin Evert on 2019-05-02.
//  Copyright © 2019 Anna-Karin Evert. All rights reserved.
//

import UIKit
import sharedcode

class UserTableViewController: UITableViewController, UISearchBarDelegate {

    @IBOutlet var searchBar:UISearchBar!
    var refresher: UIRefreshControl!
    
    var users = Array<UserWithAvatar>()
    internal let api = GithubApi()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.separatorStyle = .none
        tableView.tableFooterView = UIView(frame: .zero)
        
        tableView.dataSource = self
        tableView.delegate = self
        print(DispatchTime.now().uptimeNanoseconds)
        
    }
    
    func searchBarShouldBeginEditing(_ searchBar: UISearchBar) -> Bool {
        //remove hint when user has clicked
        searchBar.text = nil
        return true
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return users.count
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if self.tableView(tableView, numberOfRowsInSection: section) > 0 {
            return "Users"
        }
        return nil
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LabelCell", for: indexPath)
        
        let user = users[indexPath.row]
        cell.textLabel?.text = user.user.login
        cell.detailTextLabel?.text = user.user.repos_url
        cell.imageView?.image = user.avatar
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let user = users[tableView.indexPathForSelectedRow!.row]
        api.getRepos(
            reposUrl: user.user.repos_url,
            success: { repos in
                self.performSegue(withIdentifier: "showRepos", sender: repos)
                return KotlinUnit()},
            failure: { _ in
                return KotlinUnit()}
        )
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60.0
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        if segue.identifier == "showRepos" {
            let destinationViewController = segue.destination as! RepoTableViewController
            destinationViewController.repos = sender as! Array<Repo>
        }
    }
    
    @IBAction func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        fetchUsers(userName: searchBar.text!)
    }
    
    func fetchUsers (userName: String) {
        api.getUsers(
            success: { users in
                for user in users {
                    self.getAvatar(url: user.user.avatar_url, completionHandler: { avatar in
                        user.avatar = avatar
                    })
                }
                self.updateUsers(users: users)
                return KotlinUnit()},
            failure: {_ in
                return KotlinUnit()},
            userName: userName
        )
    }
    
    func updateUsers(users: Array<UserWithAvatar>) {
        self.users = users
        self.tableView.reloadData()
    }
    
    private func getAvatar (url: String, completionHandler: (UIImage?) -> ()) {
        if let url = URL(string: url) {
            if let data = try? Data( contentsOf:url){
                guard let avatar = UIImage(data: data) else {
                    print("Could not load image")
                    return
                }
                completionHandler(avatar)
            }
        }
    }
}

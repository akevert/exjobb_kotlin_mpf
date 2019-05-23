//
//  RepoTableViewController.swift
//  SearchGithub
//
//  Created by Anna-Karin Evert on 2019-04-12.
//  Copyright © 2019 Anna-Karin Evert. All rights reserved.
//

import UIKit
import sharedcode

class RepoTableViewController: UITableViewController {
    
    var repos: Array<Repo> = Array<Repo>()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.separatorStyle = .none
    }
    
    // MARK: - Table view data source
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return repos.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "RepoCell", for: indexPath) as! RepoTableViewCell
        
        let repo = repos[indexPath.row]
        cell.repoName?.text = repo.name
        cell.nrStars?.text = String(repo.stargazers_count)
        cell.nrWatchers.text = String(repo.watchers_count)
        cell.nrForks.text = String(repo.forks_count)
        
        return cell
    }
}

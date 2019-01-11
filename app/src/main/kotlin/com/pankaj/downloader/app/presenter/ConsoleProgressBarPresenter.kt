package com.pankaj.downloader.app.presenter

import com.pankaj.downloader.plugin.tracker.ProgressBarPresenter

class ConsoleProgressBarPresenter : ProgressBarPresenter({ result -> print("\r\b Downloaded progress $result") })
